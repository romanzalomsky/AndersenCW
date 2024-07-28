package com.zalomsky.rickandmorty.features.locations.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zalomsky.rickandmorty.databinding.ItemLocationsBinding
import com.zalomsky.rickandmorty.domain.model.Locations

class LocationsAdapter(
    private val onItemClickListener: (Int) -> Unit = {}
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
        location?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener { onItemClickListener.invoke(location.id) }
        }
    }

    inner class LocationsViewHolder(private val binding: ItemLocationsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Locations) {
            with(binding) {
                nameLocationsTextView.text = location.name
                typeLocationsTextView.text = location.type
                dimensionsTextView.text = location.dimension
                cardLocation.setOnClickListener { onItemClickListener(location.id) }
            }
        }
    }
}