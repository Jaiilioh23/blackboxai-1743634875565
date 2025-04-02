package com.example.mangareader.data.remote.dto

import com.example.mangareader.domain.model.Chapter
import com.example.mangareader.domain.model.Page
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChapterDto(
    val id: String,
    val attributes: Attributes,
    val relationships: Relationships?
) {
    fun toChapter(): Chapter {
        return Chapter(
            id = id,
            mangaId = relationships?.manga?.data?.id ?: "",
            number = attributes.number,
            title = attributes.title,
            pages = attributes.pages?.mapIndexed { index, url ->
                Page(
                    index = index + 1,
                    imageUrl = url,
                    width = 0, // Will be set after image loading
                    height = 0 // Will be set after image loading
                )
            } ?: emptyList()
        )
    }

    @JsonClass(generateAdapter = true)
    data class Attributes(
        val title: String,
        val number: Float,
        @Json(name = "pages") val pages: List<String>?
    )

    @JsonClass(generateAdapter = true)
    data class Relationships(
        val manga: MangaReference?
    )

    @JsonClass(generateAdapter = true)
    data class MangaReference(
        val data: MangaData?
    )

    @JsonClass(generateAdapter = true)
    data class MangaData(
        val id: String
    )
}