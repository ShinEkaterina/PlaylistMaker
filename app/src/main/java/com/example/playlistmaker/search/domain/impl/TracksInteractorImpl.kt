package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.data.TrackRepository
import com.example.playlistmaker.search.data.network.ErrorCode
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, ErrorCode?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.errorCode)
                }
            }

        }

    }
}
