package com.zalomsky.rickandmorty.features.locations.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zalomsky.rickandmorty.databinding.ItemLocationsBinding
import com.zalomsky.rickandmorty.domain.models.model.LocationsEntity

class LocationsAdapter(
    private val listener: Listener
) : PagingDataAdapter<LocationsEntity, LocationsAdapter.LocationsViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocationsEntity>() {
            override fun areItemsTheSame(oldItem: LocationsEntity, newItem: LocationsEntity): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: LocationsEntity, newItem: LocationsEntity): Boolean {
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
        fun bind(location: LocationsEntity, listener: Listener) {
            with(binding) {
                nameLocationsTextView.text = location.name
                typeLocationsTextView.text = location.type
                dimensionsTextView.text = location.dimension
                cardLocation.setOnClickListener { listener.onClick(location.id) }
            }
        }
    }
}