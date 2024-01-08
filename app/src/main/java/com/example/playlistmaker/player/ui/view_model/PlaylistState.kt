package com.example.playlistmaker.player.ui.view_model

import com.example.playlistmaker.common.domain.model.Playlist

sealed interface PlaylistState {
    object Empty : PlaylistState
    data class Content(val data: List<Playlist>) : PlaylistState
}