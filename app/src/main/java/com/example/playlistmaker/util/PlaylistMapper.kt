package com.example.playlistmaker.util

import androidx.core.net.toUri
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.ui.PlaylistInfo

object PlaylistMapper {
    fun map(playlist: Playlist): PlaylistInfo {
        return PlaylistInfo(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imageUri = playlist.imageUri.toString(),
            tracks = playlist.tracks,
            tracksNumber = playlist.tracksNumber
        )
    }

    fun map(playlistInfo: PlaylistInfo): Playlist {
        return Playlist(
            id = playlistInfo.id,
            name = playlistInfo.name,
            description = playlistInfo.description,
            imageUri = playlistInfo.imageUri!!.toUri(),
            tracks = playlistInfo.tracks,
            tracksNumber = playlistInfo.tracksNumber
        )
    }

}


