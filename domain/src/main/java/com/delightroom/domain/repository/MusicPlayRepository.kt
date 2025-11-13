package com.delightroom.domain.repository

import kotlinx.coroutines.flow.Flow

interface MusicPlayRepository {
    val isPlaying: Flow<Boolean>
    suspend fun play(id: Long)
    suspend fun pause()
    suspend fun stop()
    suspend fun seekTo(positionMs: Long)
    fun getCurrentPosition(): Flow<Long>
}