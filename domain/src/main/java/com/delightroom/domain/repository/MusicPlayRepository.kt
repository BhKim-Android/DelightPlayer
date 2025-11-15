package com.delightroom.domain.repository

import com.delightroom.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicPlayRepository {
    val progress: Flow<Long>
    suspend fun play(songs: List<Song>, index: Int)
    suspend fun pause()
    suspend fun resume()
    suspend fun stop()
    suspend fun seekTo(positionMs: Long)
    suspend fun next()
    suspend fun previous()
}