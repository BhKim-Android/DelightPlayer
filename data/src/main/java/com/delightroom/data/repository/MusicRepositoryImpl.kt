package com.delightroom.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.delightroom.data.paging.MusicPagingSource
import com.delightroom.domain.model.Song
import com.delightroom.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Provider

class MusicRepositoryImpl @Inject constructor(
    private val musicPagingSourceProvider: Provider<MusicPagingSource>
) : MusicRepository {
    override fun getSongList(): Flow<PagingData<Song>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false, initialLoadSize = 10),
            pagingSourceFactory = { musicPagingSourceProvider.get() }
        ).flow
}