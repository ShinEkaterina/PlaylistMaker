package com.example.playlistmaker.library.data.api

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylist(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)

}