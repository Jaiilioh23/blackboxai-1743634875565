package com.example.mangareader.data.repository

import androidx.paging.PagingSource
import com.example.mangareader.domain.model.Manga

interface MangaRepository {
    fun getPopularMangaPagingSource(): PagingSource<Int, Manga>
    suspend fun getMangaById(id: String): Manga?
    suspend fun searchManga(query: String): List<Manga>
}