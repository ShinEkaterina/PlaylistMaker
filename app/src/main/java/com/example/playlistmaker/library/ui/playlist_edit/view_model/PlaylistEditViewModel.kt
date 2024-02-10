package com.example.playlistmaker.library.ui.playlist_edit.view_model

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.domain.api.ImagesRepositoryInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.ui.playlist_create.view_model.PlaylistCreateViewModel
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    private val imagesInteractor: ImagesRepositoryInteractor,
    private val playlistInteractor: PlaylistInteractor
) : PlaylistCreateViewModel(imagesInteractor, playlistInteractor) {


    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
        }
    }

    fun deleteOldImage(oldImageUri: Uri) {
        imagesInteractor.removeImage(oldImageUri)
    }
}