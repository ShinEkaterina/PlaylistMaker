package com.example.playlistmaker.search.domain

import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.search.data.network.ErrorCode
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {

    fun searchTracks(expression: String): Flow<Pair<List<Track>?, ErrorCode?>>

}