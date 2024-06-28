package com.zalomsky.rickandmorty.features.characters

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.api.CharacterApi
import com.zalomsky.rickandmorty.databinding.FragmentCharactersBinding
import com.zalomsky.rickandmorty.domain.model.Character
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding

    private val adapter by lazy { CharactersAdapter(emptyList()) }

    private var _swipeRefreshLayout: SwipeRefreshLayout? = null
    private val swipeRefreshLayout get() = _swipeRefreshLayout!!

    private val charactersViewModel: CharactersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar?.title = resources.getText(R.string.characters_text)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.charactersList.adapter = adapter
        binding.loadingProgressBar.visibility = View.VISIBLE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener { getAllCharacters() }

        charactersViewModel.characters.observe(viewLifecycleOwner) { characterResponse ->
            val characterList = characterResponse?.results ?: emptyList()
            adapter.setCharacters(characterList)
            swipeRefreshLayout.isRefreshing = false
            binding.loadingProgressBar.visibility = View.GONE
        }
        getAllCharacters()
        clickers()
    }

    private fun getAllCharacters() = charactersViewModel.getAllCharacters()

    private fun clickers() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.sortIcon.setOnClickListener {
                val popupMenu = PopupMenu(requireContext(), binding.sortIcon)
                popupMenu.menuInflater.inflate(R.menu.pop_up_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.all_sort -> {
                            true
                        }

                        R.id.name_sort -> {
                            true
                        }

                        R.id.status_sort -> {
                            statusAlertDialog()
                            true
                        }

                        R.id.species_sort -> {
                            true
                        }

                        R.id.type_sort -> {
                            true
                        }

                        R.id.gender_sort -> {
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            }
            binding.sortedIcon.setOnClickListener {
                adapter.sortCharacters()
            }
        }
    }

    private fun statusAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.status_alert)

        val dialogView = layoutInflater.inflate(R.layout.dialog_sort_status, null)
        builder.setView(dialogView)

        builder.setPositiveButton(R.string.submit) { dialog, which ->
            val radioButtonGroup = dialogView.findViewById<RadioGroup>(R.id.radioButtonGroup)
            val selectedRadioButtonId = radioButtonGroup.checkedRadioButtonId
            val selectedRadioButton = dialogView.findViewById<RadioButton>(selectedRadioButtonId)
            val selectedOption = selectedRadioButton.text.toString()

            val status = when (selectedOption) {
                getString(R.string.status_alive) -> "Alive"
                getString(R.string.status_dead) -> "Dead"
                getString(R.string.status_unknown) -> "Unknown"
                else -> "all"
            }

            adapter.sortCharactersByStatus(status)
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _swipeRefreshLayout = null
    }
}