package com.delightroom.feature.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.delightroom.core_ui.ErrorPlaceholder
import com.delightroom.core_ui.LoadingIndicator
import com.delightroom.feature.model.UiState
import com.delightroom.feature.ui.MusicPlayViewModel
import kotlinx.serialization.Serializable

@Serializable
object ListDestination

@Composable
fun ListScreen(
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MusicPlayViewModel = hiltViewModel()
) {

    val songState = viewModel.uiStateList.collectAsStateWithLifecycle()
    val currentSong = viewModel.currentSong.collectAsStateWithLifecycle()
    Box(modifier = modifier.fillMaxSize()) {
        when (val state = songState.value) {
            is UiState.Loading -> LoadingIndicator()
            is UiState.Success -> {
                val list = state.data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(list.size) { index ->
                        val song = list[index]
                        val isPlaying = currentSong.value?.id == song.id
                        ListItem(
                            uiSong = song,
                            isPlaying = isPlaying,
                            onItemClick = { id -> onItemClick(id) })
                    }
                }
            }

            is UiState.Error -> ErrorPlaceholder(
                modifier = Modifier,
                errorMessage = state.throwable.message ?: "재생할 음원이 없습니다.",
                confirmText = "새로고침"
            ) {
                viewModel.getSongList()
            }
        }
    }
}