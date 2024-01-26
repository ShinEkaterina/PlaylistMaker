package com.example.playlistmaker.library.data.api

import com.example.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface FavoriteTracksRepository {
    suspend fun add(track: Track)
    suspend fun delete(track: Track)
    fun getAll(): Flow<List<Track>>
    fun checkFavorite(trackId: Long): Flow<Boolean>

}