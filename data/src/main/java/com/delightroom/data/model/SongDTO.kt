package com.delightroom.data.model

import com.delightroom.domain.model.Song

data class SongDTO(
    val albumArt: String,
    val title: String,
    val artist: String
)

fun SongDTO.toDomain(): Song = Song(
    albumArt = this.albumArt,
    title = this.title,
    artist = this.artist
)
