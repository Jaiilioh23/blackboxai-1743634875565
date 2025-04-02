package com.example.mangareader.data.remote.dto

import com.example.mangareader.domain.model.Manga
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MangaDto(
    val id: String,
    val attributes: Attributes,
    val relationships: Relationships? = null
) {
    fun toManga(): Manga {
        return Manga(
            id = id,
            title = attributes.titles.en ?: attributes.titles.en_jp ?: "",
            description = attributes.description ?: "",
            coverImage = attributes.posterImage?.original ?: "",
            status = attributes.status?.capitalize() ?: "Unknown",
            genres = relationships?.categories?.data?.map { it.name } ?: emptyList(),
            averageRating = attributes.averageRating ?: 0.0,
            chapterCount = attributes.chapterCount ?: 0
        )
    }

    @JsonClass(generateAdapter = true)
    data class Attributes(
        val titles: Titles,
        val description: String?,
        @Json(name = "posterImage") val posterImage: PosterImage?,
        val status: String?,
        @Json(name = "averageRating") val averageRating: Double?,
        @Json(name = "chapterCount") val chapterCount: Int?
    )

    @JsonClass(generateAdapter = true)
    data class Titles(
        val en: String?,
        @Json(name = "en_jp") val en_jp: String?,
        @Json(name = "ja_jp") val ja_jp: String?
    )

    @JsonClass(generateAdapter = true)
    data class PosterImage(
        val original: String?,
        val small: String?,
        val medium: String?,
        val large: String?
    )

    @JsonClass(generateAdapter = true)
    data class Relationships(
        val categories: CategoryList? = null
    )

    @JsonClass(generateAdapter = true)
    data class CategoryList(
        val data: List<Category>? = null
    )

    @JsonClass(generateAdapter = true)
    data class Category(
        val id: String,
        val name: String
    )
}