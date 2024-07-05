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
import android.widget.SearchView
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLocationsBinding.inflate(layoutInflater).apply {
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = resources.getText(R.string.locations_text)
                setDisplayHomeAsUpEnabled(true)
            }
            swipeRefreshLayout.setOnRefreshListener {
                locationsViewModel.resetFilters()
                adapter.refresh()
                swipeRefreshLayout.isRefreshing = false
            }
            locationsList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LocationLoaderStateAdapter(),
                footer = LocationLoaderStateAdapter()
            )
            btnLocationScrollToTop.setOnClickListener { locationsList.scrollToPosition(0) }
        }

        lifecycleScope.launch {
            locationsViewModel.locationsList.collectLatest { pagingData -> adapter.submitData(pagingData) }
        }

        adapter.addLoadStateListener { state ->
            binding.locationsList.isVisible = state.refresh != LoadState.Loading
            binding.progressLocation.isVisible = state.refresh == LoadState.Loading
        }

        setupSearchView()
        setupClickListeners()
        return binding.root
    }

    private fun setupClickListeners() {
        binding.sortLocations.setOnClickListener { showSortMenu() }
        binding.locationsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                binding.btnLocationScrollToTop.visibility = if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 0) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setupSearchView() {
        binding.searchLocations.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { updateQuery(name = it) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { updateQuery(name = it) }
                return true
            }
        })
    }

    private fun showSortMenu() {
        PopupMenu(requireContext(), binding.sortLocations).apply {
            menuInflater.inflate(R.menu.location_pop_up_menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.all_sort -> {
                        locationsViewModel.resetFilters()
                        adapter.refresh();
                        true
                    }
                    R.id.name_sort -> { showSearchDialog(); true }
                    R.id.type_sort -> { showSearchTypeDialog(); true }
                    R.id.dimension_sort -> { showSearchDimensionDialog(); true}
                    else -> false
                }
            }
        }.show()
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

    private fun showSearchTypeDialog() {
        AlertDialog.Builder(requireContext()).apply {
            val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_search, null)
            setView(dialogView)
            setPositiveButton(R.string.search) { _, _ ->
                val type = dialogView.findViewById<EditText>(R.id.editTextName).text.toString()
                updateQuery(type = type)
            }
            setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
        }.create().show()
    }

    private fun showSearchDimensionDialog() {
        AlertDialog.Builder(requireContext()).apply {
            val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_search, null)
            setView(dialogView)
            setPositiveButton(R.string.search) { _, _ ->
                val dimension = dialogView.findViewById<EditText>(R.id.editTextName).text.toString()
                updateQuery(dimension = dimension)
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
        findNavController().navigate(LocationsFragmentDirections.actionLocationsToDetailsLocationFragment(locationId))
    }

}