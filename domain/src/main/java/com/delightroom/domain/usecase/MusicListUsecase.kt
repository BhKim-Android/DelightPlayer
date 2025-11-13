package com.delightroom.domain.usecase

import androidx.paging.PagingData
import com.delightroom.domain.model.Song
import com.delightroom.domain.repository.MusicListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicListUsecase @Inject constructor(
    private val musicListRepository: MusicListRepository
) {
    operator fun invoke(): Flow<PagingData<Song>> {
        return musicListRepository.getSongList()
//        return null
    }
}