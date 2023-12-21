package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.util.PlaylistTracksConverter

@Dao
interface PlaylistDao {
    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylist(): List<PlaylistEntity>

    @Query("UPDATE playlists SET tracks = :listTracks WHERE id = :playlistId")
    @TypeConverters(PlaylistTracksConverter::class)
    suspend fun updatePlaylistTracksId(playlistId: Long, listTracks: List<String>)
}