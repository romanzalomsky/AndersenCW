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
import com.zalomsky.rickandmorty.databinding.ItemLocationsBinding
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.LocationsEntity
import com.zalomsky.rickandmorty.features.LoaderStateAdapter
import com.zalomsky.rickandmorty.features.characters.character.CharactersAdapter
import com.zalomsky.rickandmorty.features.characters.details.DetailsCharacterFragmentDirections
import com.zalomsky.rickandmorty.utils.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsLocationFragment : Fragment() {

    private lateinit var binding: FragmentDetailsLocationBinding
    private val adapter: CharactersAdapter by lazy { CharactersAdapter {
        findNavController().navigate(
            DetailsLocationFragmentDirections.actionDetailsLocationFragmentToDetailsCharacter2(it)
        )
    }}
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
                header = LoaderStateAdapter(ItemLocationsBinding::inflate),
                footer = LoaderStateAdapter(ItemLocationsBinding::inflate)
            )
        }

        val locationId = arguments?.getInt("locationId") ?: 0
        viewModel.getLocationById(locationId)

        lifecycleScope.launch {
            viewModel.location.collect { location ->
                location?.let {
                    displayLocation(it)
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

    private fun displayLocation(location: LocationsEntity) {

        val createdAt = resources.getText(R.string.createdAt_locations)
        val dimension = resources.getText(R.string.dimension_locations)
        val type = resources.getText(R.string.type_locations)
        val name = resources.getText(R.string.name_locations)

        binding.idNameTextView.text = "${name} " + location.name
        binding.idTypeTextView.text = "${type} " + location.type
        binding.idDimensionTextView.text = "${dimension} " + location.dimension
        binding.idCreatedTextView.text = "${createdAt} " + location.created
    }
}