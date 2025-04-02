package com.example.mangareader.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mangareader.R
import com.example.mangareader.databinding.FragmentMangaDetailBinding
import com.example.mangareader.domain.model.Manga
import com.example.mangareader.ui.adapters.GenreAdapter
import com.example.mangareader.util.viewLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MangaDetailFragment : Fragment() {

    private var binding by viewLifecycle<FragmentMangaDetailBinding>()
    private val args: MangaDetailFragmentArgs by navArgs()
    private val viewModel: MangaDetailViewModel by viewModels()
    private lateinit var genreAdapter: GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMangaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        genreAdapter = GenreAdapter()
        binding.genreRecyclerView.adapter = genreAdapter
    }

    private fun observeViewModel() {
        viewModel.manga.observe(viewLifecycleOwner) { manga ->
            manga?.let { bindMangaData(it) }
        }
        
        viewModel.loadManga(args.mangaId)
    }

    private fun bindMangaData(manga: Manga) {
        with(binding) {
            Glide.with(coverImage.context)
                .load(manga.coverImage)
                .placeholder(R.drawable.ic_manga_placeholder)
                .into(coverImage)

            titleText.text = manga.title
            descriptionText.text = manga.description
            statusText.text = manga.status
            ratingText.text = getString(R.string.rating_format, manga.averageRating)
            chapterCountText.text = getString(R.string.chapters_format, manga.chapterCount)

            genreAdapter.submitList(manga.genres)
        }
    }

    private fun setupClickListeners() {
        binding.readButton.setOnClickListener {
            // Navigate to chapter reader
        }
        
        binding.favoriteButton.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }
}