package com.example.mangareader.domain.model

data class Chapter(
    val id: String,
    val mangaId: String,
    val number: Float,
    val title: String,
    val pages: List<Page>,
    val readAt: Long? = null,
    val isDownloaded: Boolean = false
) {
    val displayNumber: String
        get() = if (number % 1 == 0f) {
            number.toInt().toString()
        } else {
            number.toString()
        }
}

data class Page(
    val index: Int,
    val imageUrl: String,
    val width: Int,
    val height: Int
)