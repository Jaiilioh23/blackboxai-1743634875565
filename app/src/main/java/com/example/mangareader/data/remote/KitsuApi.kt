package com.example.mangareader.data.remote

import com.example.mangareader.data.remote.dto.KitsuResponse
import com.example.mangareader.data.remote.dto.MangaDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KitsuApi {
    @GET("manga")
    suspend fun getPopularManga(
        @Query("page[limit]") limit: Int = 20,
        @Query("page[offset]") page: Int = 0,
        @Query("sort") sort: String = "-averageRating",
        @Query("include") include: String = "categories"
    ): KitsuResponse<MangaDto>

    @GET("manga/{id}")
    suspend fun getMangaById(
        @Path("id") id: String,
        @Query("include") include: String = "categories"
    ): KitsuResponse<MangaDto>

    @GET("manga")
    suspend fun searchManga(
        @Query("filter[text]") query: String,
        @Query("page[limit]") limit: Int = 20
    ): KitsuResponse<MangaDto>

    @GET("chapters")
    suspend fun getChapters(
        @Query("filter[mangaId]") mangaId: String,
        @Query("page[limit]") limit: Int = 20,
        @Query("page[offset]") offset: Int = 0
    ): KitsuResponse<ChapterDto>

    @GET("chapters/{id}")
    suspend fun getChapter(
        @Path("id") chapterId: String,
        @Query("include") include: String = "manga"
    ): KitsuResponse<ChapterDto>
}