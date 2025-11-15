package com.delightroom.feature.ui.player


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.delightroom.feature.ui.MusicPlayViewModel
import kotlinx.serialization.Serializable

@Serializable
data class PlayDestination(val id: Long)

@Composable
fun PlayScreen(
    id: Long,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MusicPlayViewModel = hiltViewModel()
) {

    LaunchedEffect(id) {
        viewModel.play(id)
    }

    val isPlaing by viewModel.isPlaying.collectAsStateWithLifecycle()
    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val duration by viewModel.duration.collectAsStateWithLifecycle()

//    var sliderPosition by remember { mutableFloatStateOf(0f) }
//    LaunchedEffect(progress, duration) {
//        sliderPosition = if (duration != 0L) progress.toFloat() / duration else 0f
//    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        currentSong?.let {
            SongInfoContent(
                currentSong = it,
                modifier = Modifier
            )
            SongProgressContent(
//                sliderPosition = sliderPosition,
                progress = progress,
                duration = duration,
//                onValueChange = { newSliderPosition ->
//                    sliderPosition = newSliderPosition
//                },
                onValueChangeFinished = { newPosition ->
                    viewModel.seekTo(positionMs = newPosition)
                }
            )
            SongControllerContent(
                onBack = {
                    if (progress < 1000L) viewModel.previous() else viewModel.seekTo(
                        positionMs = 0L
                    )
                },
                onPlayPause = { if (isPlaing) viewModel.pause() else viewModel.resume() },
                onNext = { viewModel.next() },
                isPlaying = isPlaing
            )
        }
    }
}