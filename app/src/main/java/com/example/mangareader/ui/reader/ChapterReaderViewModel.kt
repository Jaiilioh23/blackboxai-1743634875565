package com.example.mangareader.ui.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangareader.data.repository.ChapterRepository
import com.example.mangareader.data.repository.UserRepository
import com.example.mangareader.domain.model.Chapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterReaderViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _chapter = MutableStateFlow<Chapter?>(null)
    val chapter: StateFlow<Chapter?> = _chapter.asStateFlow()

    private val chapterId: String = checkNotNull(savedStateHandle["chapterId"])
    private val mangaId: String = checkNotNull(savedStateHandle["mangaId"])

    fun loadChapter(chapterId: String) {
        viewModelScope.launch {
            _chapter.value = chapterRepository.getChapter(chapterId)
        }
    }

    fun navigateToPreviousChapter() {
        // Implement chapter navigation logic
    }

    fun navigateToNextChapter() {
        // Implement chapter navigation logic
    }

    private val _downloadStatus = MutableStateFlow<DownloadStatus>(DownloadStatus.Idle)
    val downloadStatus: StateFlow<DownloadStatus> = _downloadStatus.asStateFlow()

    private var activeDownloadId: Long? = null

    fun downloadChapter() {
        viewModelScope.launch {
            _chapter.value?.let { chapter ->
                _downloadStatus.value = DownloadStatus.Starting
                chapterRepository.downloadChapter(chapter.id) { status ->
                    when (status) {
                        is DownloadManager.DownloadStatus.RUNNING -> {
                            val progress = (status.bytesDownloaded.toFloat() / status.totalBytes) * 100
                            _downloadStatus.value = DownloadStatus.InProgress(progress.toInt())
                        }
                        DownloadManager.DownloadStatus.COMPLETED -> {
                            _downloadStatus.value = DownloadStatus.Completed
                        }
                        DownloadManager.DownloadStatus.FAILED -> {
                            _downloadStatus.value = DownloadStatus.Failed
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    fun cancelDownload() {
        activeDownloadId?.let { downloadManager.cancelDownload(it) }
        _downloadStatus.value = DownloadStatus.Cancelled
    }

    fun saveReadingProgress(page: Int) {
        viewModelScope.launch {
            _chapter.value?.let {
                userRepository.setReadingProgress(mangaId, page)
            }
        }
    }

    sealed class DownloadStatus {
        object Idle : DownloadStatus()
        object Starting : DownloadStatus()
        data class InProgress(val progress: Int) : DownloadStatus()
        object Completed : DownloadStatus()
        object Failed : DownloadStatus()
        object Cancelled : DownloadStatus()
    }
}