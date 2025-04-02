package com.example.mangareader.data.repository

interface UserRepository {
    suspend fun isMangaFavorite(mangaId: String): Boolean
    suspend fun setMangaFavorite(mangaId: String, isFavorite: Boolean)
    suspend fun getReadingProgress(mangaId: String): Int?
    suspend fun setReadingProgress(mangaId: String, chapter: Int)
}