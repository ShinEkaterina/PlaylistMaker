package com.example.playlistmaker.library.ui.playlist_edit.view_model

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.ui.playlist_create.view_model.PlaylistCreateViewModel
import kotlinx.coroutines.launch
import java.io.File

class PlaylistEditViewModel(context: Context,
                            private val playlistInteractor: PlaylistInteractor
): PlaylistCreateViewModel(context, playlistInteractor) {

    fun getImage(name: String?) = File(filePath, name).toUri()

    fun updatePlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
        }
    }

    fun deleteOldImage(oldImageName: String){
        val file = File(filePath, oldImageName)
        file.delete()
    }
}