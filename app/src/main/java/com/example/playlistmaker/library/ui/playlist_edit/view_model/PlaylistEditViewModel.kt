package com.example.playlistmaker.library.ui.playlist_edit.view_model

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.domain.api.ImagesRepositoryInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.ui.playlist_create.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.library.ui.playlist_edit.fragment.NewPlaylistData
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    private val imagesInteractor: ImagesRepositoryInteractor,
    private val playlistInteractor: PlaylistInteractor
) : PlaylistCreateViewModel(imagesInteractor, playlistInteractor) {


    fun updatePlaylist(newPlaylist: Playlist, oldImageUri: Uri) {

        viewModelScope.launch {
            if (newPlaylist.imageUri != Uri.EMPTY)
                updateImage(newPlaylist.imageUri, oldImageUri)
            playlistInteractor.updatePlaylist(
                newPlaylist
            )
        }
    }

    private fun updateImage(newImageUri: Uri, oldImageUri: Uri) {
        saveImageToStorage(newImageUri)
        oldImageUri.let {
            deleteOldImage(it)
        }

    }


    fun deleteOldImage(oldImageUri: Uri) {
        imagesInteractor.removeImage(oldImageUri)
    }
}