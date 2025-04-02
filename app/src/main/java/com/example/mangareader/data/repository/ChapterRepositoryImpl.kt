package com.example.mangareader.data.repository

import com.example.mangareader.data.remote.KitsuApi
import com.example.mangareader.data.remote.dto.toChapter
import com.example.mangareader.domain.model.Chapter
import javax.inject.Inject

class ChapterRepositoryImpl @Inject constructor(
    private val api: KitsuApi,
    private val downloadManager: DownloadManager
) : ChapterRepository {

    override suspend fun getChapter(chapterId: String): Chapter? {
        return try {
            api.getChapter(chapterId).data?.firstOrNull()?.toChapter()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getChapters(mangaId: String): List<Chapter> {
        return try {
            api.getChapters(mangaId).data.map { it.toChapter() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun downloadChapter(chapterId: String) {
        try {
            val chapter = getChapter(chapterId) ?: return
            downloadManager.downloadChapter(chapter) { status ->
                // Update UI with download progress
                when (status) {
                    is DownloadManager.DownloadStatus.RUNNING -> {
                        val progress = (status.bytesDownloaded.toFloat() / status.totalBytes) * 100
                        // Post progress update to ViewModel
                    }
                    DownloadManager.DownloadStatus.COMPLETED -> {
                        // Handle download completion
                    }
                    DownloadManager.DownloadStatus.FAILED -> {
                        // Handle download failure
                    }
                    else -> {}
                }
            }
        } catch (e: Exception) {
            // Handle download error
        }
    }
}