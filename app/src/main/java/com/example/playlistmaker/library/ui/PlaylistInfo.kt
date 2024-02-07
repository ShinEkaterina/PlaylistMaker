package com.example.playlistmaker.library.ui

data class PlaylistInfo(
    val id: Long,
    val name: String,
    val description: String,
    val imageUri: String?,
    val tracks: List<String>?,
    val tracksNumber: Int = 0
)