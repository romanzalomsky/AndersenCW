package com.zalomsky.rickandmorty.features.episodes.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zalomsky.rickandmorty.databinding.ItemEpisodesBinding
import com.zalomsky.rickandmorty.domain.model.Episode

class EpisodeAdapter(
    private val onItemClickListener:(Int) -> Unit = {}
): PagingDataAdapter<Episode, EpisodeAdapter.EpisodeViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
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
        fun bind(episode: Episode) {
            with(binding) {
                nameEpisodesTextView.text = episode.name
                episodeEpisodeTextView.text = episode.episode
                airDateTextView.text = episode.air_date
                cardEpisode.setOnClickListener { onItemClickListener(episode.id) }
            }
        }
    }

    suspend fun submitCharacterList(episodes: List<Episode>) {
        val pagingData: PagingData<Episode> = PagingData.from(episodes)
        submitData(pagingData)
    }
}