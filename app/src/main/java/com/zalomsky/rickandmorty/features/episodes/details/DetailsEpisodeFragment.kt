package com.zalomsky.rickandmorty.features.episodes.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.databinding.FragmentDetailsEpisodeBinding
import com.zalomsky.rickandmorty.databinding.ItemEpisodesBinding
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
import com.zalomsky.rickandmorty.features.LoaderStateAdapter
import com.zalomsky.rickandmorty.features.characters.character.CharactersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsEpisodeFragment : Fragment() {

    private lateinit var binding: FragmentDetailsEpisodeBinding
    private val adapter: CharactersAdapter by lazy { CharactersAdapter {
        findNavController().navigate(
            DetailsEpisodeFragmentDirections.actionDetailsEpisodeFragmentToDetailsCharacter(it)
        )
    } }
    private val viewModel: DetailsEpisodeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsEpisodeBinding.inflate(layoutInflater).apply {
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = resources.getText(R.string.details_episode_text)
                setDisplayHomeAsUpEnabled(true)
            }
            swipeRefreshLayout.setOnRefreshListener {
                adapter.refresh()
                swipeRefreshLayout.isRefreshing = false
            }
            characterLocationsList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoaderStateAdapter(ItemEpisodesBinding::inflate),
                footer = LoaderStateAdapter(ItemEpisodesBinding::inflate)
            )
        }

        val episodeId = arguments?.getInt("episodeId") ?: 0
        viewModel.fetchEpisodeById(episodeId)

        lifecycleScope.launch {
            viewModel.episode.collect { episode ->
                episode?.let {
                    displayEpisodes(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.characters.collect {characters ->
                adapter.submitCharacterList(characters)
            }
        }

        return binding.root
    }

    private fun displayEpisodes(episode: EpisodeEntity) {

        val createdAt = resources.getText(R.string.createdAt_locations)
        val episodeText = resources.getText(R.string.episode_episode)
        val name = resources.getText(R.string.name_locations)
        val airDate = resources.getText(R.string.airDate_episode)

        binding.idNameTextView.text = "${name} " + episode.name
        binding.idAirDateTextView.text = "${airDate} " + episode.air_date
        binding.idEpisodeTextView.text = "${episodeText} " + episode.episode
        binding.idCreatedEpisodeTextView.text = "${createdAt} " + episode.created
    }

}