package com.example.playlistmaker.util

import androidx.core.net.toUri
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity
import com.example.playlistmaker.common.domain.model.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imageName = playlist.imageUri.toString(),
            tracks = playlist.tracks,
            tracksNumber = playlist.tracksNumber
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {

        return Playlist(
            playlistEntity.id,
            playlistEntity.name,
            playlistEntity.description,
            playlistEntity.imageName!!.toUri(),
            playlistEntity.tracks?: listOf(),
            playlistEntity.tracksNumber
        )
    }

    fun map(playlistEntity: List<PlaylistEntity>): List<Playlist> {
        return playlistEntity.map { playlistEntity ->
            map(playlistEntity)
        }
    }
}