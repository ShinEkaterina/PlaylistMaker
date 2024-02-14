package com.example.playlistmaker.util

import androidx.core.net.toUri
import android.net.Uri

import androidx.room.TypeConverter

object UriConverter {

    @TypeConverter
    fun fromUri(uri: Uri?): String? = uri?.toString()

    @TypeConverter
    fun toUri(data: String?): Uri? = data?.toUri()
}