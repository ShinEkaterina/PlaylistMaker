package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.data.FavoriteTracksRepository
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.util.TrackDbMapper
import com.example.playlistmaker.player.presentation.model.TrackInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackMapper: TrackDbMapper
) : FavoriteTracksRepository {

    override suspend fun add(track: Track) {
        appDatabase.trackDao().insertTrack(trackMapper.map(track))
    }

    override suspend fun delete(track: Track) {
        appDatabase.trackDao().deleteTrack(trackMapper.map(track))
    }

    override fun getAll(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { tracks -> trackMapper.map(tracks) }
    }
}