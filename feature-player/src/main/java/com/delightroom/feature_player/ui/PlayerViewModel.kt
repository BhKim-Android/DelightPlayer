package com.delightroom.feature_player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delightroom.domain.usecase.MusicPlayerUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    musicPlayerUsecase: MusicPlayerUsecase
) : ViewModel() {
    val currentSong = musicPlayerUsecase.currentSong
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Lazily,
            initialValue = 0L
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
}