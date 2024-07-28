package com.zalomsky.rickandmorty.features

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zalomsky.rickandmorty.R

open class LoaderStateAdapter<T : ViewBinding>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> T
) : LoadStateAdapter<LoaderStateAdapter<T>.ItemViewHolder>() {

    override fun getStateViewType(loadState: LoadState) = when (loadState) {
        is LoadState.NotLoading -> error(R.string.not_supported)
        LoadState.Loading -> PROGRESS
        is LoadState.Error -> ERROR
    }

    override fun onBindViewHolder(holder: ItemViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemViewHolder {
        return when (loadState) {
            LoadState.Loading -> ProgressViewHolder(inflateBinding(LayoutInflater.from(parent.context), parent, false))
            is LoadState.Error -> ErrorViewHolder(inflateBinding(LayoutInflater.from(parent.context), parent, false))
            is LoadState.NotLoading -> error(R.string.not_supported)
        }
    }

    private companion object {
        private const val ERROR = 1
        private const val PROGRESS = 0
    }

    abstract inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(loadState: LoadState)
    }

    inner class ProgressViewHolder internal constructor(
        private val binding: T
    ) : ItemViewHolder(binding.root) {

        override fun bind(loadState: LoadState) {}
    }

    inner class ErrorViewHolder internal constructor(
        private val binding: T
    ) : ItemViewHolder(binding.root) {

        override fun bind(loadState: LoadState) {
            require(loadState is LoadState.Error)
        }
    }
}