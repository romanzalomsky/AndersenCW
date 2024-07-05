package com.zalomsky.rickandmorty.features.characters.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.databinding.FragmentDetailsCharacterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsCharacterFragment : Fragment() {

    private lateinit var binding: FragmentDetailsCharacterBinding

    private val detailsViewModel: DetailsCharacterViewModel by viewModels()

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

        val createdAt = resources.getText(R.string.createdAt_characters)
        val origin = resources.getText(R.string.origin_characters)
        val location = resources.getText(R.string.location_characters)

        lifecycleScope.launch {
            detailsViewModel.getCharacterById(characterId)
                .collect { character ->
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
                        originPlace.setOnClickListener {}
                    }
                }
        }

        return binding.root
    }
}