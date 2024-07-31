package com.zalomsky.rickandmorty.features.episodes.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zalomsky.rickandmorty.databinding.ItemEpisodesBinding
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity

class EpisodeAdapter(
    private val onItemClickListener:(Int) -> Unit = {}
): PagingDataAdapter<EpisodeEntity, EpisodeAdapter.EpisodeViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EpisodeEntity>() {
            override fun areItemsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: EpisodeEntity, newItem: EpisodeEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = getItem(position)
        episode?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener { onItemClickListener.invoke(episode.id) }
        }
    }

    override fun getItemCount(): Int = snapshot().items.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodeViewHolder {
        val binding = ItemEpisodesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    inner class EpisodeViewHolder(private val binding: ItemEpisodesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(episodeEntity: EpisodeEntity) {
            with(binding) {
                nameEpisodesTextView.text = episodeEntity.name
                episodeEpisodeTextView.text = episodeEntity.episode
                airDateTextView.text = episodeEntity.air_date
                cardEpisode.setOnClickListener { onItemClickListener(episodeEntity.id) }
            }
        }
    }

    suspend fun submitCharacterList(episodeEntities: List<EpisodeEntity>) {
        val pagingData: PagingData<EpisodeEntity> = PagingData.from(episodeEntities)
        submitData(pagingData)
    }
}