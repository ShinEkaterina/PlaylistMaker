package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.TrackRepository
import com.example.playlistmaker.data.search.network.TrackSearchResponse
import com.example.playlistmaker.data.search.network.TracksSearchRequest
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.ErrorCode
import com.example.playlistmaker.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error(ErrorCode.NO_INTERNET,"Check internet connection")
            }
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                })
            }

            else -> {
                Resource.Error(ErrorCode.UNKNOWN_ERROR, "Server error")
            }
        }
    }
}

