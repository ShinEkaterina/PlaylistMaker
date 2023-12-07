package com.example.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val artistName: String,
    val duration: String,
    val artworkUrl: String,
    val collectionName: String,
    val releaseYear: String,
    val genreName: String,
    val country: String,
    val previewUrl: String
)
