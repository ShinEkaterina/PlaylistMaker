package com.example.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.playlistmaker.common.data.db.entity.TrackEntity
import com.example.playlistmaker.common.data.db.entity.TracksPlaylistEntity
import com.example.playlistmaker.util.TracksPlaylistConverter

@Dao
@TypeConverters(TracksPlaylistConverter::class)
interface TracksPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(trackPlaylistEntity: TracksPlaylistEntity)

    @Query("SELECT * FROM tracks_playlist WHERE playlistId = :playlistId")
    suspend fun getTracks(playlistId: Long): List<TracksPlaylistEntity>

    @Query("DELETE FROM tracks_playlist WHERE playlistId = :playlistId AND track = :track")
    suspend fun deleteTrack(playlistId: Long, track: TrackEntity)

    @Query("DELETE FROM tracks_playlist WHERE playlistId = :playlistId")
    suspend fun deleteTracksByPlaylist(playlistId: Long)

}