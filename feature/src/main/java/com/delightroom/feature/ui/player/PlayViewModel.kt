package com.delightroom.feature.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delightroom.domain.usecase.MusicPlayUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val musicPlayUsecase: MusicPlayUsecase
) : ViewModel() {

    val progress: StateFlow<Long> = musicPlayUsecase.progress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Lazily,
            initialValue = 0L
        )

    fun play() = viewModelScope.launch {
        musicPlayUsecase.play(songs = , index =)
    }
}