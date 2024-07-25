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
import com.zalomsky.rickandmorty.features.characters.adapters.CharactersAdapter
import com.zalomsky.rickandmorty.features.episodes.adapters.EpisodeAdapter
import com.zalomsky.rickandmorty.features.locations.details.DetailsLocationFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsEpisodeFragment : Fragment(), CharactersAdapter.Listener {

    private lateinit var binding: FragmentDetailsEpisodeBinding
    private val adapter: CharactersAdapter by lazy(LazyThreadSafetyMode.NONE) { CharactersAdapter(this) }
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
                header = LocationLoaderStateAdapter(),
                footer = LocationLoaderStateAdapter()
            )
        }

        val episodeId = arguments?.getInt("episodeId") ?: 0

        lifecycleScope.launch {
            viewModel.characters.collect {characters ->
                adapter.submitCharacterList(characters)
            }
        }

        lifecycleScope.launch {
            viewModel.getEpisodeById(episodeId).collect { character ->
                viewModel.fetchCharacters(character.characters)
            }
        }

        val createdAt = resources.getText(R.string.createdAt_locations)
        val episodeText = resources.getText(R.string.episode_episode)
        val name = resources.getText(R.string.name_locations)
        val airDate = resources.getText(R.string.airDate_episode)

        lifecycleScope.launch {
            viewModel.getEpisodeById(episodeId)
                .collect { episode ->
                    with(binding) {
                        idNameTextView.text = "${name} " + episode.name
                        idAirDateTextView.text = "${airDate} " + episode.air_date
                        idEpisodeTextView.text = "${episodeText} " + episode.episode
                        idCreatedEpisodeTextView.text = "${createdAt} " + episode.created
                    }
                }
        }

        return binding.root
    }

    override fun onClick(characterId: Int) {
        findNavController().navigate(
            DetailsEpisodeFragmentDirections.actionDetailsEpisodeFragmentToDetailsCharacter(characterId)
        )
    }
}