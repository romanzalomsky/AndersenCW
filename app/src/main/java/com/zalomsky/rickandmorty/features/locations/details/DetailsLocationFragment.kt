package com.zalomsky.rickandmorty.features.locations.details

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
import com.zalomsky.rickandmorty.databinding.FragmentDetailsLocationBinding
import com.zalomsky.rickandmorty.features.characters.adapters.CharactersAdapter
import com.zalomsky.rickandmorty.features.characters.details.DetailsCharacterFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsLocationFragment : Fragment(), CharactersAdapter.Listener {

    private lateinit var binding: FragmentDetailsLocationBinding
    private val adapter: CharactersAdapter by lazy(LazyThreadSafetyMode.NONE) { CharactersAdapter(this) }
    private val viewModel: DetailsLocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsLocationBinding.inflate(layoutInflater).apply {
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = resources.getText(R.string.details_location_text)
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

        val locationId = arguments?.getInt("locationId") ?: 0

        lifecycleScope.launch {
            viewModel.characters.collect {characters ->
                adapter.submitCharacterList(characters)
            }
        }

        lifecycleScope.launch {
            viewModel.getLocationById(locationId).collect { character ->
                viewModel.fetchCharacters(character.residents)
            }
        }

        val createdAt = resources.getText(R.string.createdAt_locations)
        val dimension = resources.getText(R.string.dimension_locations)
        val type = resources.getText(R.string.type_locations)
        val name = resources.getText(R.string.name_locations)

        lifecycleScope.launch {
            viewModel.getLocationById(locationId)
                .collect { location ->
                    with(binding) {
                        idNameTextView.text = "${name} " + location.name
                        idTypeTextView.text = "${type} " + location.type
                        idDimensionTextView.text = "${dimension} " + location.dimension
                        idCreatedTextView.text = "${createdAt} " + location.created
                    }
                }
        }
        return binding.root
    }

    override fun onClick(characterId: Int) {
        findNavController().navigate(
            DetailsLocationFragmentDirections.actionDetailsLocationFragmentToDetailsCharacter2(characterId)
        )
    }
}