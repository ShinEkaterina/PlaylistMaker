package com.example.playlistmaker.player.presentation.model

data class TrackInfo(
    val id: Long,
    val name: String,
    val artistName: String,
    val duration: String,
    val artworkUrl: String,
    val collectionName: String,
    val releaseYear: String,
    val genreName: String,
    val country: String,
    val previewUrl: String,
    val ifFavorite: Boolean
)