package com.example.mangareader.data.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.mangareader.domain.model.Chapter
import com.example.mangareader.domain.model.Page
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class DownloadManager @Inject constructor(
    private val context: Context
) {
    private val systemDownloadManager: DownloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
    private val activeDownloads = mutableMapOf<Long, DownloadStatus>()

    suspend fun downloadChapter(chapter: Chapter, onProgress: (DownloadStatus) -> Unit): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val chapterDir = createChapterDirectory(chapter)
                val downloadIds = chapter.pages.map { page ->
                    downloadPage(page, chapterDir)
                }
                
                // Track download progress
                var completed = 0
                while (completed < downloadIds.size) {
                    downloadIds.forEach { id ->
                        when (activeDownloads[id]) {
                            DownloadStatus.COMPLETED -> completed++
                            DownloadStatus.FAILED -> return@withContext false
                            else -> {
                                val status = checkDownloadStatus(id)
                                activeDownloads[id] = status
                                onProgress(status)
                            }
                        }
                    }
                    delay(1000)
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    fun cancelDownload(downloadId: Long) {
        systemDownloadManager.remove(downloadId)
        activeDownloads.remove(downloadId)
    }

    private fun createChapterDirectory(chapter: Chapter): File {
        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "manga/${chapter.mangaId}/${chapter.id}"
        )
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun downloadPage(page: Page, destinationDir: File): Long {
        val request = DownloadManager.Request(page.imageUrl.toUri())
            .setDestinationUri(
                Uri.fromFile(
                    File(destinationDir, "${page.index}.jpg")
                )
            )
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        
        val downloadId = systemDownloadManager.enqueue(request)
        activeDownloads[downloadId] = DownloadStatus.PENDING
        return downloadId
    }

    private fun checkDownloadStatus(downloadId: Long): DownloadStatus {
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = systemDownloadManager.query(query)
        return if (cursor.moveToFirst()) {
            when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.COMPLETED
                DownloadManager.STATUS_FAILED -> DownloadStatus.FAILED
                DownloadManager.STATUS_RUNNING -> DownloadStatus.RUNNING(
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)),
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                )
                else -> DownloadStatus.PENDING
            }
        } else {
            DownloadStatus.FAILED
        }.also { cursor.close() }
    }

    sealed class DownloadStatus {
        object PENDING : DownloadStatus()
        object COMPLETED : DownloadStatus()
        object FAILED : DownloadStatus()
        data class RUNNING(
            val bytesDownloaded: Int,
            val totalBytes: Int
        ) : DownloadStatus()
    }
}