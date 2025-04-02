package com.example.mangareader.ui.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.mangareader.R
import com.example.mangareader.databinding.FragmentChapterReaderBinding
import com.example.mangareader.domain.model.Chapter
import com.example.mangareader.ui.adapters.ChapterPageAdapter
import com.example.mangareader.util.viewLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChapterReaderFragment : Fragment() {

    private var binding by viewLifecycle<FragmentChapterReaderBinding>()
    private val args: ChapterReaderFragmentArgs by navArgs()
    private val viewModel: ChapterReaderViewModel by viewModels()
    private lateinit var pageAdapter: ChapterPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChapterReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        observeViewModel()
        setupControls()
    }

    private fun setupViewPager() {
        pageAdapter = ChapterPageAdapter()
        binding.viewPager.apply {
            adapter = pageAdapter
            offscreenPageLimit = 2
        }
    }

    private fun observeViewModel() {
        viewModel.chapter.observe(viewLifecycleOwner) { chapter ->
            chapter?.let { bindChapterData(it) }
        }
        viewModel.loadChapter(args.chapterId)
    }

    private fun bindChapterData(chapter: Chapter) {
        pageAdapter.submitList(chapter.pages)
        binding.chapterTitle.text = chapter.title
    }

    private fun setupControls() {
        binding.prevChapterButton.setOnClickListener {
            viewModel.navigateToPreviousChapter()
        }
        
        binding.nextChapterButton.setOnClickListener {
            viewModel.navigateToNextChapter()
        }

        binding.downloadButton.setOnClickListener {
            viewModel.downloadChapter()
        }

        observeDownloadStatus()
    }

    private fun observeDownloadStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.downloadStatus.collect { status ->
                when (status) {
                    is ChapterReaderViewModel.DownloadStatus.InProgress -> {
                        binding.downloadProgress.apply {
                            visibility = View.VISIBLE
                            progress = status.progress
                        }
                    }
                    ChapterReaderViewModel.DownloadStatus.Completed -> {
                        binding.downloadProgress.visibility = View.GONE
                        Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()
                    }
                    ChapterReaderViewModel.DownloadStatus.Failed -> {
                        binding.downloadProgress.visibility = View.GONE
                        Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                    }
                    else -> binding.downloadProgress.visibility = View.GONE
                }
            }
        }
    }
}