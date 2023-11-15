package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.TrackRepository
import com.example.playlistmaker.data.search.network.ErrorCode
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksInteractor
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
