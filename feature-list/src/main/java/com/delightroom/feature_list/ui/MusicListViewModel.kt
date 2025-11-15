package com.delightroom.feature_list.ui

import androidx.lifecycle.ViewModel
import com.delightroom.domain.usecase.MusicListUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MusicListViewModel @Inject constructor(
    musicListUsecase: MusicListUsecase
) : ViewModel() {
    val flowPagingSong = musicListUsecase()
}