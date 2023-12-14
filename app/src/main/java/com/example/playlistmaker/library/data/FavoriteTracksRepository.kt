package com.example.playlistmaker.library.data

import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.player.presentation.model.TrackInfo
import kotlinx.coroutines.flow.Flow


interface FavoriteTracksRepository {
    suspend fun add(track: Track)
    suspend fun delete(track: Track)
    fun getAll(): Flow<List<Track>>
}