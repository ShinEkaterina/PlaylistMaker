package com.example.playlistmaker.library.ui.playlist.view_model

import com.example.playlistmaker.common.domain.model.Playlist

sealed interface PlaylistsFragmentState {
    object Empty : PlaylistsFragmentState
    object Loading : PlaylistsFragmentState
    data class Content(val data: List<Playlist>) : PlaylistsFragmentState
}