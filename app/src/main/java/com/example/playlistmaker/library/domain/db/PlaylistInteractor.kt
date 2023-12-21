package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.common.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getAllPlaylist(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
}