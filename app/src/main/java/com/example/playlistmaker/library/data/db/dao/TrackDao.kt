package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT id FROM tracks")
    suspend fun getTracksId(): List<Long>

    @Query("SELECT * FROM tracks where id = :trackId")
    suspend fun isTrackFavorite(trackId:Long): TrackEntity?
}