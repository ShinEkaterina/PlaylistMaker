package com.example.playlistmaker.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.playlistmaker.util.TracksPlaylistConverter

@Entity(tableName = "tracks_playlist")
data class TracksPlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val playlistId: Long,
    @TypeConverters(TracksPlaylistConverter::class)
    val track: TrackEntity
)