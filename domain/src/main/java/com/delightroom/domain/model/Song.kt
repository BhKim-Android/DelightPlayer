package com.delightroom.domain.model

data class Song(
    val id: Long,
    val content: String,
    val albumArt: String,
    val title: String,
    val artist: String
)
