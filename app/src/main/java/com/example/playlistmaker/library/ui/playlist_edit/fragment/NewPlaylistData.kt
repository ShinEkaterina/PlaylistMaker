package com.example.playlistmaker.library.ui.playlist_edit.fragment

import android.net.Uri

data class NewPlaylistData(
    val id: Long,
    val newImage:Uri = Uri.EMPTY,
    val newTitle:String,
    val newDescription:String? = null
)
