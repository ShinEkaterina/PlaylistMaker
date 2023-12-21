package com.example.playlistmaker.library.ui.view_model.playlist

import androidx.lifecycle.ViewModel

class PlaylistCreateViewModel(private val libraryInteractor: LibraryInteractor) : ViewModel() {

    suspend fun addPlaylist(playlist: Playlist) {
        libraryInteractor.addPlaylist(playlist)
    }
}