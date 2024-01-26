package com.example.playlistmaker.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val artistName: String,
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl: String,
    val collectionName: String,
    val releaseDate: String,// Год релиза трека
    val genreName: String,
    val country: String,
    val previewUrl: String
)
