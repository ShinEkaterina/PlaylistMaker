package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.data.api.PlaylistRepository
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylist()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

}