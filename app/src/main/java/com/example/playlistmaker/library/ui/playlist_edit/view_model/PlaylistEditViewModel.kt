package com.example.playlistmaker.library.ui.playlist_edit.view_model

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.domain.api.ImagesRepositoryInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.ui.playlist_create.view_model.PlaylistCreateViewModel
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    context: Context,
    private val imagesInteractor: ImagesRepositoryInteractor,
    private val playlistInteractor: PlaylistInteractor
) : PlaylistCreateViewModel(context, imagesInteractor, playlistInteractor) {


    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
        }
    }

    fun deleteOldImage(oldImageUri: Uri) {
        imagesInteractor.removeImage(oldImageUri)
    }
}