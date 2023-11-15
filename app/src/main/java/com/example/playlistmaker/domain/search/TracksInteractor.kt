package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.data.search.network.ErrorCode
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {

    fun searchTracks(expression: String): Flow<Pair<List<Track>?, ErrorCode?>>

}