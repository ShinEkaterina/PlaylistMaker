package com.example.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.util.PlaylistTracksConverter

@Dao
interface PlaylistDao {
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylist(): List<PlaylistEntity>

    @Query("UPDATE playlists SET tracks = :listTracks WHERE id = :playlistId")
    @TypeConverters(PlaylistTracksConverter::class)
    suspend fun updatePlaylistTracksId(playlistId: Long, listTracks: List<String>)

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): PlaylistEntity

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)
}