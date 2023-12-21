package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.data.api.PlaylistRepository
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.util.PlaylistDbMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistMapper: PlaylistDbMapper
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
}