package com.example.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.playlistmaker.common.data.db.entity.TracksPlaylistEntity
import com.example.playlistmaker.util.TracksPlaylistConverter

@Dao
@TypeConverters(TracksPlaylistConverter::class)
interface TracksPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(trackPlaylistEntity: TracksPlaylistEntity)

    @Query("SELECT * FROM tracks_playlist WHERE playlistId = :playlistId")
    suspend fun getTracks(playlistId: Long): List<TracksPlaylistEntity>

}