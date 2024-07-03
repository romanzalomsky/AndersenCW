package com.zalomsky.rickandmorty.features.characters

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.databinding.FragmentCharactersBinding
import com.zalomsky.rickandmorty.features.characters.adapters.CharacterLoaderStateAdapter
import com.zalomsky.rickandmorty.features.characters.adapters.CharactersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment(), CharactersAdapter.Listener {

    private lateinit var binding: FragmentCharactersBinding
    private val adapter: CharactersAdapter by lazy(LazyThreadSafetyMode.NONE) { CharactersAdapter(this) }
    private val charactersViewModel: CharactersViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCharactersBinding.inflate(layoutInflater).apply {
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = getString(R.string.characters_text)
                setDisplayHomeAsUpEnabled(false)
            }
            swipeRefreshLayout.setOnRefreshListener {
                adapter.refresh()
                swipeRefreshLayout.isRefreshing = false
            }
            charactersList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = CharacterLoaderStateAdapter(),
                footer = CharacterLoaderStateAdapter()
            )
            btnScrollToTop.setOnClickListener { charactersList.scrollToPosition(0) }
        }

        lifecycleScope.launch {
            charactersViewModel.charactersList.collectLatest{ pagingData -> adapter.submitData(pagingData) }
        }

        adapter.addLoadStateListener { state ->
            binding.charactersList.isVisible = state.refresh != LoadState.Loading
            binding.centerProgressBar.isVisible = state.refresh == LoadState.Loading
        }

        setupClickListeners()
        return binding.root
    }

    private fun setupClickListeners() {
        binding.sortCharacters.setOnClickListener { showSortMenu() }
        binding.charactersList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                binding.btnScrollToTop.visibility = if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 0) View.VISIBLE else View.GONE
            }
        })
    }

    private fun showSortMenu() {
        PopupMenu(requireContext(), binding.sortCharacters).apply {
            menuInflater.inflate(R.menu.pop_up_menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.all_sort -> { adapter.refresh(); true }
                    R.id.name_sort -> { showSearchDialog(); true }
                    R.id.status_sort -> { showFilterDialog(R.layout.dialog_sort_status, ::statusSelection); true }
                    R.id.species_sort -> { showFilterDialog(R.layout.dialog_species, ::speciesSelection); true }
                    R.id.gender_sort -> { showFilterDialog(R.layout.dialog_gender, ::genderSelection); true }
                    else -> false
                }
            }
        }.show()
    }

    private fun showFilterDialog(layoutRes: Int, handleSelection: (View) -> Unit) {
        AlertDialog.Builder(requireContext()).apply {
            val dialogView = requireActivity().layoutInflater.inflate(layoutRes, null)
            setView(dialogView)
            setPositiveButton(R.string.submit) { _, _ -> handleSelection(dialogView) }
            setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
        }.create().show()
    }

    private fun statusSelection(dialogView: View) {
        val status = when (dialogView.findViewById<RadioGroup>(R.id.radioButtonGroup).checkedRadioButtonId) {
            R.id.status_alive -> getString(R.string.status_alive)
            R.id.status_dead -> getString(R.string.status_dead)
            R.id.status_unknown -> getString(R.string.status_unknown)
            else -> ""
        }
        updateQuery(status = status)
    }

    private fun speciesSelection(dialogView: View) {
        val species = when (dialogView.findViewById<RadioGroup>(R.id.radioButtonGroupSpecies).checkedRadioButtonId) {
            R.id.radioButtonHuman -> getString(R.string.human)
            R.id.radioButtonAlien -> getString(R.string.alien)
            R.id.radioButtonHumanoid -> getString(R.string.humanoid)
            R.id.radioButtonUnknown -> getString(R.string.unknown)
            R.id.radioButtonPoopybutthole -> getString(R.string.poopybutthole)
            R.id.radioButtonMythological_creature -> getString(R.string.mythological_creature)
            R.id.radioButtonAnimal -> getString(R.string.animal)
            R.id.radioButtonRobot -> getString(R.string.robot)
            R.id.radioButtonCronenberg -> getString(R.string.cronenberg)
            R.id.radioButtonDisease -> getString(R.string.disease)
            else -> ""
        }
        updateQuery(species = species)
    }

    private fun genderSelection(dialogView: View) {
        val gender = when (dialogView.findViewById<RadioGroup>(R.id.radioButtonGroupGender).checkedRadioButtonId) {
            R.id.radioButtonFemale -> getString(R.string.female)
            R.id.radioButtonMale -> getString(R.string.male)
            R.id.radioButtonGenderless -> getString(R.string.genderless)
            R.id.radioButtonUnknownGender -> getString(R.string.unknown)
            else -> ""
        }
        updateQuery(gender = gender)
    }

    private fun showSearchDialog() {
        AlertDialog.Builder(requireContext()).apply {
            val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_search, null)
            setView(dialogView)
            setPositiveButton(R.string.search) { _, _ ->
                val name = dialogView.findViewById<EditText>(R.id.editTextName).text.toString()
                updateQuery(name = name)
            }
            setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
        }.create().show()
    }

    private fun updateQuery(name: String = charactersViewModel.query.value.name, status: String = charactersViewModel.query.value.status, species: String = charactersViewModel.query.value.species, gender: String = charactersViewModel.query.value.gender) {
        charactersViewModel.updateQuery(name, status, species, gender)
    }

    override fun onClick(characterId: Int) {
        findNavController().navigate(CharactersFragmentDirections.actionCharactersToDetailsCharacterFragment(characterId))
    }
}