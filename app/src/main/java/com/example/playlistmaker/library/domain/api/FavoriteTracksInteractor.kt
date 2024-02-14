package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    suspend fun add(track: Track)
    suspend fun delete(track: Track)
    fun getAll(): Flow<List<Track>>
    fun checkFavorite(trackId: Long):  Flow<Boolean>
}