package com.example.playlistmaker.common.domain.model

import android.net.Uri

data class Playlist(
    val id: Long,
    val name: String,
    val description: String,
    val imageUri: Uri?,
    val tracks: List<String> = listOf(),
    val tracksNumber: Int = 0
)