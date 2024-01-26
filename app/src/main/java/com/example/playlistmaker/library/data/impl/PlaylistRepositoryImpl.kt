package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.data.api.PlaylistRepository
import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.common.data.db.entity.TracksPlaylistEntity
import com.example.playlistmaker.util.PlaylistDbMapper
import com.example.playlistmaker.util.TrackDbMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistMapper: PlaylistDbMapper,
    private val trackDbConverter: TrackDbMapper,
) : PlaylistRepository {
    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return flow {
            val playlists = appDatabase.playlistDao().getAllPlaylist().reversed()
            emit(playlistMapper.map(playlists))
        }
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(playlistMapper.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        withContext(Dispatchers.IO) {
            val listTracksId = ArrayList<String>()
            if (!playlist.tracks.isNullOrEmpty())
                listTracksId.addAll(playlist.tracks)
            listTracksId.add(0, track.trackId.toString())

            appDatabase.playlistDao().updatePlaylistTracksId(
                playlistMapper.map(playlist).id,
                listTracksId
            )
            val trackEntity = trackDbConverter.map(track)
            appDatabase.tracksPlaylistDao().addTrack(
                TracksPlaylistEntity(playlistId = playlist.id, track = trackEntity)
            )
        }
    }
}