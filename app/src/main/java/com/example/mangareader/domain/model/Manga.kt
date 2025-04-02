package com.example.mangareader.domain.model

data class Manga(
    val id: String,
    val title: String,
    val description: String,
    val coverImage: String,
    val status: String,
    val genres: List<String>,
    val averageRating: Double,
    val chapterCount: Int,
    val isFavorite: Boolean = false,
    val lastReadChapter: Int? = null,
    val lastReadAt: Long? = null
) {
    companion object {
        val STATUS_ONGOING = "Ongoing"
        val STATUS_COMPLETED = "Completed"
        val STATUS_HIATUS = "Hiatus"
        val STATUS_CANCELLED = "Cancelled"
    }
}