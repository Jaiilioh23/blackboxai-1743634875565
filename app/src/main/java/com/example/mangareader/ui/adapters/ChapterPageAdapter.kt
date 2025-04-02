package com.example.mangareader.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mangareader.databinding.ItemPageBinding
import com.example.mangareader.domain.model.Page

class ChapterPageAdapter : RecyclerView.Adapter<ChapterPageAdapter.PageViewHolder>() {

    private var pages: List<Page> = emptyList()

    fun submitList(pages: List<Page>) {
        this.pages = pages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemPageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size

    class PageViewHolder(
        private val binding: ItemPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(page: Page) {
            Glide.with(binding.root.context)
                .load(page.imageUrl)
                .into(binding.pageImage)
        }
    }
}