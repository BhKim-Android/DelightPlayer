package com.delightroom.domain.usecase

import com.delightroom.domain.model.Song
import com.delightroom.domain.repository.MusicPlayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicPlayUsecase @Inject constructor(
    private val musicPlayRepository: MusicPlayRepository
) {
    val progress: Flow<Long> get() = musicPlayRepository.progress

    suspend fun play(songs: List<Song>, index: Int) {
        musicPlayRepository.play(songs, index)
    }

    suspend fun pause() = musicPlayRepository.pause()
    suspend fun resume() = musicPlayRepository.resume()
    suspend fun stop() = musicPlayRepository.stop()
    suspend fun seekTo(positionMs: Long) = musicPlayRepository.seekTo(positionMs)
    suspend fun next() = musicPlayRepository.next()
    suspend fun previous() = musicPlayRepository.previous()
}