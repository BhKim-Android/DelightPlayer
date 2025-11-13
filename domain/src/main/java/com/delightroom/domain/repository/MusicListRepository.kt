package com.delightroom.domain.repository

import androidx.paging.PagingData
import com.delightroom.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicListRepository {
    fun getSongList(): Flow<PagingData<Song>>
}