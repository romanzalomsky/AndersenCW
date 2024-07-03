package com.zalomsky.rickandmorty.features.characters.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.databinding.FragmentDetailsCharacterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsCharacterFragment : Fragment() {

    private lateinit var binding: FragmentDetailsCharacterBinding

    private val detailsViewModel: DetailsCharacterViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsCharacterBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar?.title = resources.getText(R.string.details_character_text)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val image = binding.imageView
        val characterId = arguments?.getInt("characterId") ?: 0
        detailsViewModel.getCharacterById(characterId)
        detailsViewModel.characterById.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it.image)
                .into(image)
            with(binding){
                namePlace.text = it.name
                statusPlace.text = it.status
                speciesPlace.text = it.species
                genderPlace.text = it.gender
                originPlace.text = "${it.origin.name}, ${it.origin.url}"
                locationPlace.text = "${it.location.name}, ${it.location.url}"
                episodePlace.text = it.episode.toString()
                urlPlace.text = it.url
                createdPlace.text = it.created
            }
        }
        return binding.root
    }
}