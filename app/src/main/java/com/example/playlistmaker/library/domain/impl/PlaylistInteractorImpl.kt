package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.data.api.PlaylistRepository
import com.example.playlistmaker.library.domain.api.ImagesRepositoryInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val imagesRepositoryInteractor: ImagesRepositoryInteractor
) : PlaylistInteractor {
    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylist()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistRepository.addTrackToPlaylist(playlist, track)
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist> {
        return playlistRepository.getPlaylist(playlistId)
    }

    override fun getTracksPlaylist(playlistId: Long): Flow<List<Track>> {
        return playlistRepository.getTracksPlaylist(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, track: Track) {
        playlistRepository.deleteTrackFromPlaylist(playlistId, track)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlist.imageUri?.let { imagesRepositoryInteractor.removeImage(it) }

        playlistRepository.deletePlaylist(playlist.id)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

}