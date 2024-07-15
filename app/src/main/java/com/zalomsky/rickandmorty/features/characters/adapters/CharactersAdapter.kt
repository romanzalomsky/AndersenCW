package com.zalomsky.rickandmorty.features.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zalomsky.rickandmorty.databinding.ItemCharacterBinding
import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.model.Episode

class CharactersAdapter(
    private val listener: Listener
) : PagingDataAdapter<Character, CharactersAdapter.CharactersViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharactersViewHolder(binding)
    }

    override fun getItemCount(): Int = snapshot().items.size

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = getItem(position)
        character?.let { holder.bind(it, listener) }
    }

    interface Listener {
        fun onClick(characterId: Int)
    }

    inner class CharactersViewHolder(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character, listener: Listener) {
            with(binding) {
                nameTextView.text = character.name
                statusTextView.text = character.status
                speciesTextView.text = character.species
                genderTextView.text = character.gender
                Glide.with(root.context).load(character.image).into(charactersImage)
                charactersImage.setOnClickListener { listener.onClick(character.id) }
            }
        }
    }

    suspend fun submitCharacterList(characters: List<Character>) {
        val pagingData: PagingData<Character> = PagingData.from(characters)
        submitData(pagingData)
    }
}