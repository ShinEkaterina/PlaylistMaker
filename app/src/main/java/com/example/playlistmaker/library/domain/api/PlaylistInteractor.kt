package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getAllPlaylist(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    fun getPlaylist(playlistId: Long): Flow<Playlist>
    fun getTracksPlaylist(playlistId: Long): Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(playlistId: Long, track: Track)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun updatePlaylist(playlist: Playlist)
}

