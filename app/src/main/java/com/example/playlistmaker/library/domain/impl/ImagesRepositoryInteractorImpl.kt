package com.example.playlistmaker.library.domain.impl

import android.net.Uri
import com.example.playlistmaker.library.data.api.ImagesRepository
import com.example.playlistmaker.library.domain.api.ImagesRepositoryInteractor

class ImagesRepositoryInteractorImpl(
    val repository: ImagesRepository
) : ImagesRepositoryInteractor {
    override fun saveImage(uri: Uri): String {
        return repository.saveImage(uri)
    }

    override fun clearAllImages() {
        repository.clearAllImages()
    }

    override fun removeImage(uri: Uri): Boolean {
        return repository.removeImage(uri)
    }
}