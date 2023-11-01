package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.TrackRepository
import com.example.playlistmaker.data.search.network.TrackSearchResponse
import com.example.playlistmaker.data.search.network.TracksSearchRequest
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.data.search.network.ErrorCode
import com.example.playlistmaker.util.Resource


const val SUCCESS_RESULT_CODE = 200
const val NO_INTERNET_RESULT_CODE = -1


class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        when (response.resultCode) {
            NO_INTERNET_RESULT_CODE -> {
                return Resource.Error(ErrorCode.NO_INTERNET, NO_INTERNET_MESSAGE)
            }

            SUCCESS_RESULT_CODE -> {
                if ((response as TrackSearchResponse).results.isEmpty()) {
                    return Resource.Error(
                        ErrorCode.NOTHING_FOUND,
                        NOTHING_FOUND_MESSAGE
                    )
                } else {
                    return Resource.Success((response as TrackSearchResponse).results.map {
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
            }

            else -> {
                return Resource.Error(ErrorCode.UNKNOWN_ERROR, UNKNOWN_ERROR_MESSAGE)
            }
        }
    }

    companion object {
        private const val UNKNOWN_ERROR_MESSAGE = "Server error"
        private const val NO_INTERNET_MESSAGE = "Check internet connection"
        private const val NOTHING_FOUND_MESSAGE = "Nothing found. Results list is empty"
    }
}

