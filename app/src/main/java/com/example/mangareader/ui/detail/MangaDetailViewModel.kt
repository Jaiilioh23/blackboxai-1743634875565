package com.example.mangareader.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangareader.data.repository.MangaRepository
import com.example.mangareader.data.repository.UserRepository
import com.example.mangareader.domain.model.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaDetailViewModel @Inject constructor(
    private val mangaRepository: MangaRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _manga = MutableStateFlow<Manga?>(null)
    val manga: StateFlow<Manga?> = _manga.asStateFlow()

    private val mangaId: String = checkNotNull(savedStateHandle["mangaId"])

    fun loadManga(mangaId: String) {
        viewModelScope.launch {
            val manga = mangaRepository.getMangaById(mangaId)?.copy(
                isFavorite = userRepository.isMangaFavorite(mangaId)
            )
            _manga.value = manga
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _manga.value?.let { manga ->
                val newFavoriteState = !manga.isFavorite
                userRepository.setMangaFavorite(manga.id, newFavoriteState)
                _manga.value = manga.copy(isFavorite = newFavoriteState)
            }
        }
    }
}