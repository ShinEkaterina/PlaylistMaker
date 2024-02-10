package com.example.playlistmaker.library.ui.playlist_create.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.domain.api.ImagesRepositoryInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor


open class PlaylistCreateViewModel(
    private val imagesInteractor: ImagesRepositoryInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {


    suspend fun addPlaylist(playlist: Playlist) {
        playlistInteractor.addPlaylist(playlist)
    }

    fun saveImageToStorage(uri: Uri): String {
        return imagesInteractor.saveImage(uri)
    }
}
