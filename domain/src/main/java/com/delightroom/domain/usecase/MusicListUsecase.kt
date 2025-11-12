package com.delightroom.domain.usecase

import androidx.paging.PagingData
import com.delightroom.domain.model.Song
import com.delightroom.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicListUsecase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(): Flow<PagingData<Song>> {
        return musicRepository.getSongList()
//        return null
    }
}