package com.delightroom.feature.ui.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.delightroom.domain.model.Song

@Composable
fun SongInfoContent(
    currentSong: Song,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        AsyncImage(
            model = currentSong.albumArt,
            contentDescription = "AlbumArt",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(15.dp))
        )

        Text(
            text = currentSong.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = currentSong.artist, fontSize = 12.sp
        )
    }
}