package com.zalomsky.rickandmorty.features.characters.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.databinding.FragmentCharactersBinding
import com.zalomsky.rickandmorty.databinding.FragmentDetailsCharacterBinding
import com.zalomsky.rickandmorty.features.characters.CharactersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsCharacterFragment : Fragment() {

    private lateinit var binding: FragmentDetailsCharacterBinding

    private val viewModel: CharactersViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CharactersViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsCharacterBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar?.title = resources.getText(R.string.details_character_text)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val image = binding.imageView
        val characterId = arguments?.getInt("characterId") ?: 0
        viewModel.getCharacterById(characterId)
        viewModel.charactersById.observe(viewLifecycleOwner) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}