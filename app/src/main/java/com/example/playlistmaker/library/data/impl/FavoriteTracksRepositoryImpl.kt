package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.data.api.FavoriteTracksRepository
import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.common.data.db.entity.TrackEntity
import com.example.playlistmaker.util.TrackDbConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackMapper: TrackDbConverter
) : FavoriteTracksRepository {

    override suspend fun add(track: Track) {
        appDatabase.trackDao().insertTrack(trackMapper.map(track))
    }

    override suspend fun delete(track: Track) {
        appDatabase.trackDao().deleteTrack(trackMapper.map(track))
    }

    override fun getAll(): Flow<List<Track>> {
        return flow {
            val tracks = appDatabase.trackDao().getTracks().reversed()
            emit(convertFromTrackEntity(tracks))
        }
    }

    override fun checkFavorite(trackId: Long): Flow<Boolean> {
        return flow {
            val answer = appDatabase.trackDao().isTrackFavorite(trackId) != null
            emit(answer)
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { tracks -> trackMapper.map(tracks) }
    }
}