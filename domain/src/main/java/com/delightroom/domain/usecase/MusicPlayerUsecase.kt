package com.delightroom.domain.usecase

import com.delightroom.domain.model.Song
import com.delightroom.domain.repository.MusicPlayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicPlayerUsecase @Inject constructor(
    private val musicPlayRepository: MusicPlayRepository
) {
    val currentSong: Flow<Song?> get() = musicPlayRepository.currentSong
    val progress: Flow<Long> get() = musicPlayRepository.progress
    val duration: Flow<Long> get() = musicPlayRepository.duration

    // Player Controller
    suspend fun play(startIndex: Int) = musicPlayRepository.play(startIndex)
    suspend fun pause() = musicPlayRepository.pause()
    suspend fun resume() = musicPlayRepository.resume()
    suspend fun stop() = musicPlayRepository.stop()
    suspend fun seekTo(positionMs: Long) = musicPlayRepository.seekTo(positionMs)
    suspend fun next() = musicPlayRepository.next()
    suspend fun previous() = musicPlayRepository.previous()
}