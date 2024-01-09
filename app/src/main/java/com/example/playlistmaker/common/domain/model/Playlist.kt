package com.example.playlistmaker.common.domain.model

data class Playlist(
    val id: Long,
    val name: String,
    val description: String,
    val imageName: String?,
    val tracks: List<String>?,
    val tracksNumber: Int = 0
)