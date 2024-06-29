package com.zalomsky.rickandmorty.features.characters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.domain.model.Character
import java.util.Collections

class CharactersAdapter(
    private var characters: List<Character>,
    private val listener: Listener
) : RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>(),
    ItemMoveCallback.ItemTouchHelperAdapter {

    interface Listener {
        fun onClick(characterId: Int)
    }

    class CharactersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameText: TextView = itemView.findViewById(R.id.nameTextView)
        private val statusText: TextView = itemView.findViewById(R.id.statusTextView)
        private val speciesText: TextView = itemView.findViewById(R.id.speciesTextView)
        private val genderText: TextView = itemView.findViewById(R.id.genderTextView)
        private val image: ImageView = itemView.findViewById(R.id.charactersImage)

        fun bind(character: Character, listener: Listener) {
            nameText.text = character.name
            statusText.text = character.status
            speciesText.text = character.species
            genderText.text = character.gender
            Glide.with(itemView.context)
                .load(character.image)
                .into(image)
            image.setOnClickListener {
                listener.onClick(character.id)
            }
        }
    }

    fun setCharacters(characters: List<Character>) {
        this.characters = characters
        notifyDataSetChanged()
    }

    fun sortCharacters() {
        characters = characters.sortedBy { it.name }
        notifyDataSetChanged()
    }

    fun sortCharactersByStatus(status: String) {
        characters = if (status == "all") {
            characters
        } else {
            characters.filter { it.status.equals(status, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharactersViewHolder(view)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(characters[position], listener)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(characters, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(characters, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}