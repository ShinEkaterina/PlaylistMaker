package com.example.playlistmaker.library.ui.playlist.view_model

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.ui.playlist.fragment.PlaylistCreateFragment.Companion.PLAYLIST_STORAGE_NAME
import java.io.File


class PlaylistCreateViewModel(
    context: Context,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val filePath by lazy {
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PLAYLIST_STORAGE_NAME)
    }

    suspend fun addPlaylist(playlist: Playlist) {
        playlistInteractor.addPlaylist(playlist)
    }
}
