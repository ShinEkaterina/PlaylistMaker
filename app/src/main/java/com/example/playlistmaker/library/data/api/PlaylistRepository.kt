package com.example.playlistmaker.library.data.api

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylist(): Flow<List<Playlist>>
    fun getTracksPlaylist(playlistId: Long): Flow<List<Track>>

    fun getPlaylist(playlistId: Long): Flow<Playlist>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    suspend fun deletePlaylist(playlistId: Long)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deleteTrackFromPlaylist(playlistId: Long, track: Track)


}