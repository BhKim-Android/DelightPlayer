package com.delightroom.domain.repository

import com.delightroom.domain.model.Song

interface MusicListRepository {
    suspend fun getSongList(): Result<List<Song>>
}