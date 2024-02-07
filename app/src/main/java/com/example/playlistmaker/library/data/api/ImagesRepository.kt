package com.example.playlistmaker.library.data.api

import android.net.Uri

interface ImagesRepository {
    fun saveImage(uri: Uri): String
    fun clearAllImages()
    fun removeImage(uri: Uri): Boolean
}