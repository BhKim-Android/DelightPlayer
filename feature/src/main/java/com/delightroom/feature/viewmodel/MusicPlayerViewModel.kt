package com.delightroom.feature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.map
import com.delightroom.domain.usecase.MusicListUsecase
import com.delightroom.feature.model.UiSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicListUsecase: MusicListUsecase
) : ViewModel() {

    val flowPagingSong = musicListUsecase().map { pagingData ->
        pagingData.map { song ->
            UiSong(albumArt = song.albumArt, title = song.title, artist = song.artist)
        }
    }

}