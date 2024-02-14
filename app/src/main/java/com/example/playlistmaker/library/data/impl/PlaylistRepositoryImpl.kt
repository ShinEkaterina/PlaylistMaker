package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.data.api.PlaylistRepository
import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.common.data.db.entity.TracksPlaylistEntity
import com.example.playlistmaker.util.PlaylistDbConverter
import com.example.playlistmaker.util.TrackDbConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: TrackDbConverter,
) : PlaylistRepository {
    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return flow {
            val playlists = appDatabase.playlistDao().getAllPlaylist().reversed()
            emit(playlistDbConverter.map(playlists))
        }
    }

    override fun getTracksPlaylist(playlistId: Long): Flow<List<Track>> {
        return flow {
            val tracksPlaylistEntityList =
                appDatabase.tracksPlaylistDao().getTracks(playlistId).sortedBy { it.id }
            val tracks = tracksPlaylistEntityList.map { it.track }.reversed()
            emit(trackDbConverter.map(tracks))
        }
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist> {
        return flow {
            val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
            emit(playlistDbConverter.map(playlist))
        }
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        withContext(Dispatchers.IO) {
            val listTracksId = ArrayList<String>()
            if (playlist.tracks.isNotEmpty())
                listTracksId.addAll(playlist.tracks)
            listTracksId.add(0, track.trackId.toString())

            appDatabase.playlistDao().updatePlaylistTracksId(
                playlistDbConverter.map(playlist).id,
                listTracksId
            )
            val trackEntity = trackDbConverter.map(track)
            appDatabase.tracksPlaylistDao().addTrack(
                TracksPlaylistEntity(playlistId = playlist.id, track = trackEntity)
            )
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        appDatabase.playlistDao().deletePlaylist(playlistId)
        appDatabase.tracksPlaylistDao().deleteTracksByPlaylist(playlistId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, track: Track) {
        withContext(Dispatchers.IO) {
            val listTracksId = ArrayList<String>()
            val listTracks =
                appDatabase.playlistDao().getPlaylistById(playlistId).tracks?.toMutableList()?: mutableListOf()
            listTracks.remove(track.trackId.toString())
            listTracksId.addAll(listTracks.toList())
            appDatabase.playlistDao().updatePlaylistTracksId(playlistId, listTracksId)

            appDatabase.tracksPlaylistDao().deleteTrack(playlistId, trackDbConverter.map(track))
        }
    }
}