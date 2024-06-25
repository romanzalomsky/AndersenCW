package com.zalomsky.rickandmorty.features.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.domain.model.Character

class CharactersAdapter(private var characters: List<Character>): RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    class CharactersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val idText: TextView = itemView.findViewById(R.id.idTextView)
        private val nameText: TextView = itemView.findViewById(R.id.nameTextView)
        private val statusText: TextView = itemView.findViewById(R.id.statusTextView)
        private val speciesText: TextView = itemView.findViewById(R.id.speciesTextView)
        private val genderText: TextView = itemView.findViewById(R.id.genderTextView)
        private val image: ImageView = itemView.findViewById(R.id.charactersImage)

        fun bind(character: Character) {
            idText.text = character.id.toString()
            nameText.text = character.name
            statusText.text = character.status
            speciesText.text = character.species
            genderText.text = character.gender
            Picasso.get()
                .load(character.image)
                .into(image)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_character, parent, false)
        return CharactersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(characters[position])
    }
}