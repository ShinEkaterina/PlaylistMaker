package com.example.playlistmaker.util

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.data.db.entity.PlaylistEntity

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imageName = playlist.imageName,
            tracks = playlist.tracks,
            tracksNumber = playlist.tracksNumber
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.id,
            playlistEntity.name,
            playlistEntity.description,
            playlistEntity.imageName,
            playlistEntity.tracks,
            playlistEntity.tracksNumber
        )
    }

    fun map(playlistEnity: List<PlaylistEntity>): List<Playlist> {
        return playlistEnity.map { playlistEnity ->
            map(playlistEnity)
        }
    }
}