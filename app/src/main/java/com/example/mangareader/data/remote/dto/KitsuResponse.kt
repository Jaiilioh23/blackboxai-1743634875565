package com.example.mangareader.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KitsuResponse<T>(
    val data: List<T>,
    val meta: Meta? = null,
    val links: Links? = null
) {
    @JsonClass(generateAdapter = true)
    data class Meta(
        val count: Int
    )

    @JsonClass(generateAdapter = true)
    data class Links(
        val first: String?,
        val prev: String?,
        val next: String?,
        val last: String?
    )
}