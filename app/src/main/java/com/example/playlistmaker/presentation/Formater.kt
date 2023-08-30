package com.example.playlistmaker.presentation

import java.text.SimpleDateFormat
import java.util.Locale

object Formater {
    fun formayMillsTimeToDuration(millsTima: Long): String = SimpleDateFormat(
        "mm:ss", Locale.getDefault()
    ).format(millsTima)
}