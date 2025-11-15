package com.delightroom.domain.repository

import com.delightroom.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicPlayRepository {
    val isPlaying: Flow<Boolean>
    val currentSong: Flow<Song?> // 재생중인 곡정보, null인경우 재생중이 아님.
    val progress: Flow<Long>    // 재생시간.
    val duration: Flow<Long>
    suspend fun play(id: Long, songs: List<Song>)
    suspend fun pause()
    suspend fun resume()
    suspend fun stop()
    suspend fun seekTo(positionMs: Long)
    suspend fun next()
    suspend fun previous()
}