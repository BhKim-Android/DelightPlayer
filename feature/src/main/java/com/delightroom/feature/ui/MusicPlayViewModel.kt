package com.delightroom.feature.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delightroom.domain.model.Song
import com.delightroom.domain.usecase.MusicListUsecase
import com.delightroom.domain.usecase.MusicPlayerUsecase
import com.delightroom.feature.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayViewModel @Inject constructor(
    private val musicListUsecase: MusicListUsecase,
    private val musicPlayerUsecase: MusicPlayerUsecase
) : ViewModel() {

    private val _uiStateList = MutableStateFlow<UiState<List<Song>>>(UiState.Loading)
    val uiStateList: StateFlow<UiState<List<Song>>> = _uiStateList

    private val songList = mutableListOf<Song>()    // 재생목록을 만들기 위한 리스트

    init {
        getSongList()
    }

    fun getSongList() = viewModelScope.launch {
        musicListUsecase()
            .onSuccess {
                songList.addAll(it)
                _uiStateList.value = UiState.Success(it)
            }
            .onFailure {
                songList.clear()
                _uiStateList.value = UiState.Error(it)
            }
    }

    val isPlaying = musicPlayerUsecase.isPlaing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Eagerly,
            initialValue = false
        )

    val currentSong = musicPlayerUsecase.currentSong
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Eagerly,
            initialValue = null
        )

    val progress = musicPlayerUsecase.progress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Lazily,
            initialValue = 0L
        )

    val duration = musicPlayerUsecase.duration
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Lazily,
            initialValue = 0L
        )

    fun play(id: Long) {
        if (currentSong.value?.id == id) return
        viewModelScope.launch {
            musicPlayerUsecase.play(id, songList)
        }
    }

    fun pause() = viewModelScope.launch {
        musicPlayerUsecase.pause()
    }

    fun resume() = viewModelScope.launch {
        musicPlayerUsecase.resume()
    }

    fun stop() = viewModelScope.launch {
        musicPlayerUsecase.stop()
    }

    fun seekTo(positionMs: Long) = viewModelScope.launch {
        musicPlayerUsecase.seekTo(positionMs)
    }

    fun next() = viewModelScope.launch {
        musicPlayerUsecase.next()
    }

    fun previous() = viewModelScope.launch {
        musicPlayerUsecase.previous()
    }
}