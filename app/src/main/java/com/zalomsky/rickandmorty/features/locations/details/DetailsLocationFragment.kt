package com.zalomsky.rickandmorty.features.locations.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.databinding.FragmentDetailsLocationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsLocationFragment : Fragment() {

    private lateinit var binding: FragmentDetailsLocationBinding
    private val viewModel: DetailsLocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsLocationBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar?.title = resources.getText(R.string.details_location_text)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val locationId = arguments?.getInt("locationId") ?: 0

        lifecycleScope.launch {
            viewModel.getLocationById(locationId)
                .collect { location ->
                    with(binding) {
                        idNameTextView.text = location.name
                        idTypeTextView.text = location.type
                        idDimensionTextView.text = location.dimension
                        idUrlTextView.text = location.url
                        idCreatedTextView.text = location.created
                        idResidentsTextView.text = location.residents.toString()
                    }
                }
        }
        return binding.root
    }
}