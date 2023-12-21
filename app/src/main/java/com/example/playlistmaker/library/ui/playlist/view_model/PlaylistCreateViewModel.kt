package com.example.playlistmaker.library.ui.playlist.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.domain.db.PlaylistInteractor

class PlaylistCreateViewModel(private val libraryInteractor: PlaylistInteractor) : ViewModel() {

    suspend fun addPlaylist(playlist: Playlist) {
        libraryInteractor.addPlaylist(playlist)
    }
}