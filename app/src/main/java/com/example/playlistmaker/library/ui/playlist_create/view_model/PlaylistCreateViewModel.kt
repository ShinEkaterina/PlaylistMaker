package com.example.playlistmaker.library.ui.playlist_create.view_model

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.ui.playlist_create.fragment.PlaylistCreateFragment.Companion.PLAYLIST_STORAGE_NAME
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


open class PlaylistCreateViewModel(
    context: Context,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

     val filePath by lazy {
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PLAYLIST_STORAGE_NAME)
    }

    suspend fun addPlaylist(playlist: Playlist) {
        playlistInteractor.addPlaylist(playlist)
    }

    fun saveImageToStorage(playlistImgName: String, image: Bitmap) {
        viewModelScope.launch {

            if (!filePath.exists()) {
                filePath.mkdirs()
            }

            val file = File(filePath, playlistImgName)
            val outputStream = FileOutputStream(file)

            image.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        }
    }
}
