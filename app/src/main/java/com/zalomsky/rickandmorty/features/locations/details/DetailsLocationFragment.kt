package com.zalomsky.rickandmorty.features.locations.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.databinding.FragmentDetailsLocationBinding
import com.zalomsky.rickandmorty.features.locations.LocationsViewModel

class DetailsLocationFragment : Fragment() {

    private lateinit var binding: FragmentDetailsLocationBinding
    private val viewModel: LocationsViewModel by lazy {
        ViewModelProvider(requireActivity()).get(LocationsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsLocationBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar?.title = resources.getText(R.string.details_location_text)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val locationId = arguments?.getInt("locationId") ?: 0
        viewModel.getLocationById(locationId)
        viewModel.locationsById.observe(viewLifecycleOwner) {
            with(binding){
                idNameTextView.text = it.name
                idTypeTextView.text = it.type
                idDimensionTextView.text = it.dimension
                idUrlTextView.text = it.url
                idCreatedTextView.text = it.created
                idResidentsTextView.text = it.residents.toString()
            }
        }
        return binding.root
    }
}