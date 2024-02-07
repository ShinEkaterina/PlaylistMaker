package com.example.playlistmaker.util

import androidx.core.net.toUri
import android.net.Uri

import androidx.room.TypeConverter

object UriConverter {

    @TypeConverter
    fun fromUri(uri:Uri?): String? =
        if (uri == null)
            null
        else
            uri.toString()

    @TypeConverter
    fun toUri(data: String?): Uri? = data?.toUri()
}