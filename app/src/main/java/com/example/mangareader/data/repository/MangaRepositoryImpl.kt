package com.example.mangareader.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mangareader.data.remote.KitsuApi
import com.example.mangareader.data.remote.dto.toManga
import com.example.mangareader.domain.model.Manga
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
    private val api: KitsuApi
) : MangaRepository {

    override fun getPopularMangaPagingSource(): PagingSource<Int, Manga> {
        return MangaPagingSource(api)
    }

    override suspend fun getMangaById(id: String): Manga? {
        return try {
            api.getMangaById(id).data?.toManga()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun searchManga(query: String): List<Manga> {
        return try {
            api.searchManga(query).data.map { it.toManga() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

class MangaPagingSource(
    private val api: KitsuApi
) : PagingSource<Int, Manga>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Manga> {
        return try {
            val page = params.key ?: 0
            val response = api.getPopularManga(page = page)
            val mangaList = response.data.map { it.toManga() }

            LoadResult.Page(
                data = mangaList,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (mangaList.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Manga>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}