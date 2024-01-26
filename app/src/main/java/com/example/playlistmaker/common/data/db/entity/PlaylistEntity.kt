package com.example.playlistmaker.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.playlistmaker.util.PlaylistTracksConverter

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String,
    val imageName: String?,
    @TypeConverters(PlaylistTracksConverter::class)
    val tracks: List<String>? = null,
    val tracksNumber: Int = 0
)