package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}