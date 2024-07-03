package com.zalomsky.rickandmorty.features.locations.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zalomsky.rickandmorty.databinding.ItemCharacterBinding
import com.zalomsky.rickandmorty.databinding.ItemLocationsBinding
import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.model.Locations

class LocationsAdapter(
    private val listener: Listener
) : PagingDataAdapter<Locations, LocationsAdapter.LocationsViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Locations>() {
            override fun areItemsTheSame(oldItem: Locations, newItem: Locations): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Locations, newItem: Locations): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val binding = ItemLocationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationsViewHolder(binding)
    }

    override fun getItemCount(): Int = snapshot().items.size

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        val location = getItem(position)
        location?.let { holder.bind(it, listener) }
    }

    interface Listener {
        fun onClick(locationId: Int)
    }

    inner class LocationsViewHolder(private val binding: ItemLocationsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(Location: Locations, listener: Listener) {
            with(binding) {
                nameLocationsTextView.text = Location.name
                typeLocationsTextView.text = Location.type
                dimensionsTextView.text = Location.dimension
/*                charactersImage.setOnClickListener { listener.onClick(character.id) }*/
            }
        }
    }
}