package com.delightroom.domain.usecase

import com.delightroom.domain.repository.MusicPlayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class MusicPlayUsecase @Inject constructor(
    private val musicPlayRepository: MusicPlayRepository
) {
    val isPlaying: Flow<Boolean> get() = musicPlayRepository.isPlaying

    suspend fun play(id: Long) {
        musicPlayRepository.getCurrentPosition().firstOrNull()?.let { pos ->
            if (pos > 0) musicPlayRepository.stop()
        }
        musicPlayRepository.play(id)
    }

    suspend fun pause() = musicPlayRepository.pause()

    suspend fun stop() = musicPlayRepository.stop()

    suspend fun seekTo(positionMs: Long) = musicPlayRepository.seekTo(positionMs)
}