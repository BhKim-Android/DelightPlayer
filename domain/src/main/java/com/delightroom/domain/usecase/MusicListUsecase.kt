package com.delightroom.domain.usecase

import com.delightroom.domain.model.Song
import com.delightroom.domain.repository.MusicListRepository
import javax.inject.Inject

class MusicListUsecase @Inject constructor(
    private val musicListRepository: MusicListRepository
) {
    suspend operator fun invoke(): Result<List<Song>> {
        return musicListRepository.getSongList()
    }
}