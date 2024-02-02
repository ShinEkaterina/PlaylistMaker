package com.example.playlistmaker.library.ui.playlist.view_model

import com.example.playlistmaker.common.domain.model.Track

sealed class PlaylistTracksState {
    object Empty : PlaylistTracksState()
    object Loading : PlaylistTracksState()
    data class Content(val data: List<Track>) : PlaylistTracksState()
}