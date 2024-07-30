package com.zalomsky.rickandmorty.features.characters.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.databinding.FragmentDetailsCharacterBinding
import com.zalomsky.rickandmorty.features.LocationLoaderStateAdapter
import com.zalomsky.rickandmorty.features.episodes.EpisodeViewModel
import com.zalomsky.rickandmorty.features.episodes.adapters.EpisodeAdapter
import com.zalomsky.rickandmorty.features.locations.details.DetailsLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsCharacterFragment : Fragment(), EpisodeAdapter.Listener {

    private lateinit var binding: FragmentDetailsCharacterBinding
    private val adapter: EpisodeAdapter by lazy(LazyThreadSafetyMode.NONE) { EpisodeAdapter(this) }
    private val detailsViewModel: DetailsCharacterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsCharacterBinding.inflate(layoutInflater).apply {
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = resources.getText(R.string.details_character_text)
                setDisplayHomeAsUpEnabled(true)
            }
            swipeRefreshLayout.setOnRefreshListener {
                adapter.refresh()
                swipeRefreshLayout.isRefreshing = false
            }
            episodesCharacterList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LocationLoaderStateAdapter(),
                footer = LocationLoaderStateAdapter()
            )
        }

        val characterId = arguments?.getInt("characterId") ?: 0

        lifecycleScope.launch {
            detailsViewModel.episodes.collect {episodes ->
                adapter.submitCharacterList(episodes)
            }
        }

        lifecycleScope.launch {
            detailsViewModel.getCharacterById(characterId).collect { character ->
                detailsViewModel.fetchEpisodes(character.episode)
            }
        }

        setupClickers(characterId)
        return binding.root
    }

    private fun setupClickers(
        characterId: Int
    ) {
        val image = binding.imageView
        val createdAt = resources.getText(R.string.createdAt_characters)
        val origin = resources.getText(R.string.origin_characters)
        val location = resources.getText(R.string.location_characters)

        lifecycleScope.launch {
            detailsViewModel.getCharacterById(characterId).collect { character ->
                Glide.with(image)
                    .clear(image)
                Glide.with(image)
                    .load(character.image)
                    .into(image)
                with(binding) {
                    namePlace.text = character.name
                    statusPlace.text = character.status
                    speciesPlace.text = character.species
                    genderPlace.text = character.gender
                    originPlace.text = "${origin}" + " ${character.origin.name}"
                    locationPlace.text = "${location}" + " ${character.location.name}"
                    createdPlace.text = "${createdAt}" + " ${character.created}"

                    val locationId = extractLocationId(character.location.url)
                    originPlace.setOnClickListener {
                        findNavController().navigate(
                            DetailsCharacterFragmentDirections.actionDetailsCharacterToDetailsLocationFragment(locationId!!)
                        )
                    }
                    locationPlace.setOnClickListener {
                        findNavController().navigate(
                            DetailsCharacterFragmentDirections.actionDetailsCharacterToDetailsLocationFragment(locationId!!)
                        )
                    }
                }
            }
        }
    }

    private fun extractLocationId(url: String): Int? {
        val regex = """.*https://rickandmortyapi.com/api/location/(\d+).*""".toRegex()
        val matchResult = regex.matchEntire(url)
        return matchResult?.groups?.get(1)?.value?.toInt()
    }

    override fun onClick(episodeId: Int) {
        findNavController().navigate(
            DetailsCharacterFragmentDirections.actionDetailsCharacterToDetailsEpisodeFragment(episodeId)
        )
    }
}