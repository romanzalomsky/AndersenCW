package com.zalomsky.rickandmorty.features.characters.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zalomsky.rickandmorty.databinding.ItemCharacterBinding
import com.zalomsky.rickandmorty.domain.model.CharacterEntity
import com.zalomsky.rickandmorty.utils.load

class CharactersAdapter(
    private val onItemClickListener: (Int) -> Unit = {}
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
        character?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener { onItemClickListener.invoke(character.id) }
        }
    }

    inner class CharactersViewHolder(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterEntity) {
            with(binding) {
                nameTextView.text = character.name
                statusTextView.text = character.status
                speciesTextView.text = character.species
                genderTextView.text = character.gender
                charactersImage.load(character.image)
                charactersImage.setOnClickListener { onItemClickListener(character.id) }
            }
        }
    }

    suspend fun submitCharacterList(characters: List<CharacterEntity>) {
        val pagingData: PagingData<CharacterEntity> = PagingData.from(characters)
        submitData(pagingData)
    }
}