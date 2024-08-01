package com.zalomsky.rickandmorty.features.characters.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.databinding.FragmentDetailsCharacterBinding
import com.zalomsky.rickandmorty.databinding.ItemCharacterBinding
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.features.LoaderStateAdapter
import com.zalomsky.rickandmorty.features.episodes.episode.EpisodeAdapter
import com.zalomsky.rickandmorty.utils.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsCharacterFragment : Fragment() {

    private lateinit var binding: FragmentDetailsCharacterBinding
    private val adapter: EpisodeAdapter by lazy { EpisodeAdapter {
        findNavController().navigate(
            DetailsCharacterFragmentDirections.actionDetailsCharacterToDetailsEpisodeFragment(it)
        )
    } }
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
                header = LoaderStateAdapter(ItemCharacterBinding::inflate),
                footer = LoaderStateAdapter(ItemCharacterBinding::inflate)
            )
        }

        val characterId = arguments?.getInt("characterId") ?: 0
        detailsViewModel.fetchCharacterById(characterId)

        lifecycleScope.launch {
            detailsViewModel.character.collect { character ->
                character?.let {
                    displayCharacter(it)
                }
            }
        }

        lifecycleScope.launch {
            detailsViewModel.episodes.collect {episodes ->
                adapter.submitCharacterList(episodes)
            }
        }

        /*lifecycleScope.launch {
            detailsViewModel.fetchCharacterById(characterId).collect { character ->
                detailsViewModel.fetchEpisodes(character.episode)
            }
        }*/

        //todo: When u get a list,
        // you need to filter it in the fragment.
        // Get it and after filter.
        // Or you can create a filter method in adapter
        return binding.root
    }

    private fun displayCharacter(character: CharacterEntity) {

        val createdAt = resources.getText(R.string.createdAt_characters)
        val origin = resources.getText(R.string.origin_characters)
        val location = resources.getText(R.string.location_characters)

        binding.imageView.load(character.image)
        binding.namePlace.text = character.name
        binding.statusPlace.text = character.status
        binding.speciesPlace.text = character.species
        binding.genderPlace.text = character.gender
        binding.originPlace.text = "${origin} ${character.origin.name}"
        binding.locationPlace.text = "${location} ${character.location.name}"
        binding.createdPlace.text = "${createdAt} ${character.created}"

        val locationId = extractLocationId(character.location.url)
        binding.originPlace.setOnClickListener {
            findNavController().navigate(
                DetailsCharacterFragmentDirections.actionDetailsCharacterToDetailsLocationFragment(locationId!!)
            )
        }
        binding.locationPlace.setOnClickListener {
            findNavController().navigate(
                DetailsCharacterFragmentDirections.actionDetailsCharacterToDetailsLocationFragment(locationId!!)
            )
        }
    }

    private fun extractLocationId(url: String): Int? {
        val regex = """.*https://rickandmortyapi.com/api/location/(\d+).*""".toRegex()
        val matchResult = regex.matchEntire(url)
        return matchResult?.groups?.get(1)?.value?.toInt()
    }
}