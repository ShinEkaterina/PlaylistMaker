package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.data.FavoriteTracksRepository
import com.example.playlistmaker.library.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.player.presentation.model.TrackInfo
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTracksInteractor {
    override suspend fun add(track: Track) {
        favoriteTracksRepository.add(track)
    }

    override suspend fun delete(track: Track) {
        favoriteTracksRepository.delete(track)
    }

    override fun getAll(): Flow<List<Track>> {
        return favoriteTracksRepository.getAll()
    }

    override fun checkFavorite(trackId: Long): Flow<Boolean> {
        return favoriteTracksRepository.checkFavorite(trackId)
    }
}