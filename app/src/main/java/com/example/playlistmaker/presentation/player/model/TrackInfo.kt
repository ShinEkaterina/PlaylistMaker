package com.example.playlistmaker.presentation.player.model

data class TrackInfo (
    val id:Long,
    val name: String,
    val artistName: String,
    val duration: String,
    val artworkUrl: String,
    val collectionName:String,
    val releaseYear: String,
    val genreName:String,
    val country:String,
    val previewUrl: String
)