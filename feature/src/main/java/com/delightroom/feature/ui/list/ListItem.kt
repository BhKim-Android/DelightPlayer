package com.delightroom.feature.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.delightroom.domain.model.Song

@Composable
fun ListItem(
    uiSong: Song,
    modifier: Modifier = Modifier,
    onItemClick: (Long) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {  onItemClick(uiSong.id) }
    ) {
        AsyncImage(
            model = uiSong.albumArt, contentDescription = "앨범 아트",
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            Modifier
                .weight(4f)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = uiSong.title)
            Text(text = uiSong.artist)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    ListItem(
        Song(
            id = 0L,
            content = "",
            albumArt = "",
            title = "Shape of You",
            artist = "Ed Sheeran"
        )
    ) {}
}