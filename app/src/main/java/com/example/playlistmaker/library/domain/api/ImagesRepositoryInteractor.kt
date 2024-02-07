package com.example.playlistmaker.library.domain.api

import android.net.Uri

interface ImagesRepositoryInteractor {
    fun saveImage(uri: Uri): String
    fun clearAllImages()
    fun removeImage(uri: Uri): Boolean
}