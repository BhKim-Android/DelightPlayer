package com.delightroom.domain.repository

import androidx.paging.PagingData
import com.delightroom.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun getSongList(): Flow<PagingData<Song>>
}