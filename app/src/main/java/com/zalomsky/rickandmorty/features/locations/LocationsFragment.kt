package com.zalomsky.rickandmorty.features.locations

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
import com.zalomsky.rickandmorty.databinding.FragmentLocationsBinding
import com.zalomsky.rickandmorty.features.characters.CharactersFragmentDirections
import com.zalomsky.rickandmorty.features.characters.CharactersViewModel
import com.zalomsky.rickandmorty.features.characters.adapters.CharacterLoaderStateAdapter
import com.zalomsky.rickandmorty.features.characters.adapters.CharactersAdapter
import com.zalomsky.rickandmorty.features.locations.adapters.LocationLoaderStateAdapter
import com.zalomsky.rickandmorty.features.locations.adapters.LocationsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationsFragment : Fragment(), LocationsAdapter.Listener {

    private lateinit var binding: FragmentLocationsBinding
    private val adapter: LocationsAdapter by lazy(LazyThreadSafetyMode.NONE) { LocationsAdapter(this) }
    private val locationsViewModel: LocationsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLocationsBinding.inflate(layoutInflater).apply {
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = resources.getText(R.string.locations_text)
                setDisplayHomeAsUpEnabled(true)
            }
/*            swipeRefreshLayout.apply {
                setOnRefreshListener { isRefreshing = false }
            }*/
            locationsList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LocationLoaderStateAdapter(),
                footer = LocationLoaderStateAdapter()
            )
            /*btnScrollToTop.setOnClickListener { charactersList.scrollToPosition(0) }*/
        }

        lifecycleScope.launch {
            locationsViewModel.locationsList.collectLatest(adapter::submitData)
        }

        adapter.addLoadStateListener { state ->
            binding.locationsList.isVisible = state.refresh != LoadState.Loading
/*            binding.progress.isVisible = state.refresh == LoadState.Loading*/
        }

        setupClickListeners()
        return binding.root
    }

    private fun setupClickListeners() {
        binding.sortLocations.setOnClickListener { showSortMenu() }
        binding.locationsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                /*binding.btnScrollToTop.visibility = if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 0) View.VISIBLE else View.GONE*/
            }
        })
    }


    private fun showSortMenu() {
        PopupMenu(requireContext(), binding.sortLocations).apply {
            menuInflater.inflate(R.menu.location_pop_up_menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.all_sort -> { adapter.refresh(); true }
                    R.id.name_sort -> { showSearchDialog(); true }
                    R.id.type_sort -> { showFilterDialog(R.layout.dialog_sort_status, ::typeSelection); true }
                    R.id.dimension_sort -> { showFilterDialog(R.layout.dialog_species, ::dimensionSelection); true }
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

    private fun typeSelection(dialogView: View) {
        val status = when (dialogView.findViewById<RadioGroup>(R.id.radioButtonGroup).checkedRadioButtonId) {
            R.id.status_alive -> getString(R.string.status_alive)
            R.id.status_dead -> getString(R.string.status_dead)
            R.id.status_unknown -> getString(R.string.status_unknown)
            else -> ""
        }
        updateQuery(type = status)
    }

    private fun dimensionSelection(dialogView: View) {
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
        updateQuery(dimension = species)
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

    private fun updateQuery(
        name: String = locationsViewModel.query.value.name,
        type: String = locationsViewModel.query.value.type,
        dimension: String = locationsViewModel.query.value.dimension
    ) {
        locationsViewModel.updateQueryLocations(name, type, dimension)
    }

    override fun onClick(locationId: Int) {
/*        findNavController().navigate(CharactersFragmentDirections.actionCharactersToDetailsCharacterFragment(locationId))*/
    }

}