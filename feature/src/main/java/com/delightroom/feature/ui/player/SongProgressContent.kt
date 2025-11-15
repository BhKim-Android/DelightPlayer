package com.delightroom.feature.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongProgressContent(
//    sliderPosition: Float,
    progress: Long,
    duration: Long,
//    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Slider(
            value = sliderPosition,
            onValueChange = { newSliderPosition->
                sliderPosition = newSliderPosition
            },
            onValueChangeFinished = {
                if (duration != 0L) {
                    val newPosition = (sliderPosition * duration).toLong()
                    onValueChangeFinished(newPosition)
                }
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .padding(0.dp)
                        .size(16.dp)
                        .background(color = Color.Black, CircleShape),
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    thumbTrackGapSize = 0.dp,
                    modifier = Modifier.height(2.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.Gray
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = formatMillis(progress))
            Text(text = formatMillis(duration))
        }
    }
}

fun formatMillis(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}