package com.zalomsky.rickandmorty.features.episodes

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
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
import com.zalomsky.rickandmorty.databinding.FragmentEpisodesBinding
import com.zalomsky.rickandmorty.features.EpisodeLoaderStateAdapter
import com.zalomsky.rickandmorty.features.episodes.adapters.EpisodeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodesFragment : Fragment(), EpisodeAdapter.Listener {

    private lateinit var binding: FragmentEpisodesBinding
    private val adapter: EpisodeAdapter by lazy(LazyThreadSafetyMode.NONE) { EpisodeAdapter(this) }
    private val episodeViewModel: EpisodeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentEpisodesBinding.inflate(layoutInflater).apply {
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = resources.getText(R.string.episodes_text)
                setDisplayHomeAsUpEnabled(true)
            }
            swipeRefreshLayout.setOnRefreshListener {
                episodeViewModel.resetFilters()
                adapter.refresh()
                swipeRefreshLayout.isRefreshing = false
            }
            episodesList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = EpisodeLoaderStateAdapter(),
                footer = EpisodeLoaderStateAdapter()
            )
            btnEpisodeScrollToTop.setOnClickListener { episodesList.scrollToPosition(0) }
        }

        lifecycleScope.launch {
            episodeViewModel.episodesList.collectLatest { pagingData -> adapter.submitData(pagingData) }
        }

        adapter.addLoadStateListener { state ->
            binding.episodesList.isVisible = state.refresh != LoadState.Loading
            binding.progressEpisode.isVisible = state.refresh == LoadState.Loading
        }

        setupClickListeners()
        setupSearchView()
        return binding.root
    }

    private fun setupClickListeners() {
        binding.sortEpisode.setOnClickListener { showSortMenu() }
        binding.episodesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                binding.btnEpisodeScrollToTop.visibility = if ((recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 0) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setupSearchView() {
        binding.searchEpisode.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        PopupMenu(requireContext(), binding.sortEpisode).apply {
            menuInflater.inflate(R.menu.episode_pop_up_menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.all_sort -> {
                        episodeViewModel.resetFilters()
                        adapter.refresh();
                        true
                    }
                    R.id.name_episode_sort -> { showSearchDialog(); true }
                    R.id.episode_sort -> { showSearchTypeDialog(); true }
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
                val episode = dialogView.findViewById<EditText>(R.id.editTextName).text.toString()
                updateQuery(episode = episode)
            }
            setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
        }.create().show()
    }

    private fun updateQuery(
        name: String = episodeViewModel.query.value.name,
        episode: String = episodeViewModel.query.value.episode
    ) {
        episodeViewModel.updateQueryEpisodes(name, episode)
    }

    override fun onClick(episodeId: Int) {
        findNavController().navigate(EpisodesFragmentDirections.actionEpisodesToDetailsEpisodeFragment(episodeId))
    }
}