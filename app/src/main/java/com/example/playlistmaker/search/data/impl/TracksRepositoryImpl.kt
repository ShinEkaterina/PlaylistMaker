package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.TrackRepository
import com.example.playlistmaker.search.data.network.TrackSearchResponse
import com.example.playlistmaker.search.data.network.TracksSearchRequest
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.search.data.network.ErrorCode
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


const val SUCCESS_RESULT_CODE = 200
const val NO_INTERNET_RESULT_CODE = -1


class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDataBase: AppDatabase
) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        when (response.resultCode) {
            NO_INTERNET_RESULT_CODE -> {
                emit(Resource.Error(ErrorCode.NO_INTERNET, NO_INTERNET_MESSAGE))
            }

            SUCCESS_RESULT_CODE -> {
                if ((response as TrackSearchResponse).results.isEmpty()) {
                    emit(
                        Resource.Error(
                            ErrorCode.NOTHING_FOUND,
                            NOTHING_FOUND_MESSAGE
                        )
                    )
                } else {
                    val favTrackIds = appDataBase.trackDao().getTracksId()
                    emit(Resource.Success((response as TrackSearchResponse).results.map {
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
                            it.previewUrl,
                           isFavorite =  (it.trackId in favTrackIds)
                        )

                    }))
                }
            }

            else -> {
                emit(Resource.Error(ErrorCode.UNKNOWN_ERROR, UNKNOWN_ERROR_MESSAGE))
            }
        }
    }

    companion object {
        private const val UNKNOWN_ERROR_MESSAGE = "Server error"
        private const val NO_INTERNET_MESSAGE = "Check internet connection"
        private const val NOTHING_FOUND_MESSAGE = "Nothing found. Results list is empty"
    }
}

