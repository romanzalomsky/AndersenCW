package com.zalomsky.rickandmorty.features.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zalomsky.rickandmorty.databinding.ItemCharacterBinding
import com.zalomsky.rickandmorty.domain.model.CharacterEntity

class CharactersAdapter(
    private val listener: Listener
) : PagingDataAdapter<CharacterEntity, CharactersAdapter.CharactersViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CharacterEntity>() {
            override fun areItemsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity): Boolean {
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
        fun bind(characterEntity: CharacterEntity, listener: Listener) {
            with(binding) {
                nameTextView.text = characterEntity.name
                statusTextView.text = characterEntity.status
                speciesTextView.text = characterEntity.species
                genderTextView.text = characterEntity.gender
                Glide.with(root.context).load(characterEntity.image).into(charactersImage)
                charactersImage.setOnClickListener { listener.onClick(characterEntity.id) }
            }
        }
    }

    suspend fun submitCharacterList(characterEntities: List<CharacterEntity>) {
        val pagingData: PagingData<CharacterEntity> = PagingData.from(characterEntities)
        submitData(pagingData)
    }
}