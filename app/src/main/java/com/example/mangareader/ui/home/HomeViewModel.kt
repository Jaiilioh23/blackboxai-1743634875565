package com.example.mangareader.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.mangareader.data.repository.MangaRepository
import com.example.mangareader.domain.model.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mangaRepository: MangaRepository
) : ViewModel() {

    val mangaList = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 40
        ),
        pagingSourceFactory = { mangaRepository.getPopularMangaPagingSource() }
    ).flow.cachedIn(viewModelScope)
}