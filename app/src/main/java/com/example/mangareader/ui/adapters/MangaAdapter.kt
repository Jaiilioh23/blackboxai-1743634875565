package com.example.mangareader.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mangareader.R
import com.example.mangareader.databinding.ItemMangaBinding
import com.example.mangareader.domain.model.Manga
import com.example.mangareader.util.getStatusColor

class MangaAdapter(
    private val onItemClick: (Manga) -> Unit
) : PagingDataAdapter<Manga, MangaAdapter.MangaViewHolder>(MangaDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val binding = ItemMangaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MangaViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        getItem(position)?.let { manga ->
            holder.bind(manga)
        }
    }

    class MangaViewHolder(
        private val binding: ItemMangaBinding,
        private val onItemClick: (Manga) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(manga: Manga) {
            with(binding) {
                Glide.with(root.context)
                    .load(manga.coverImage)
                    .placeholder(R.drawable.ic_manga_placeholder)
                    .into(mangaCover)

                mangaTitle.text = manga.title
                mangaStatus.text = manga.status
                mangaStatus.setTextColor(root.context.getStatusColor(manga.status))
                mangaGenre.text = manga.genres.take(2).joinToString(", ")

                root.setOnClickListener { onItemClick(manga) }
            }
        }
    }

    companion object {
        private val MangaDiffCallback = object : DiffUtil.ItemCallback<Manga>() {
            override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean {
                return oldItem == newItem
            }
        }
    }
}