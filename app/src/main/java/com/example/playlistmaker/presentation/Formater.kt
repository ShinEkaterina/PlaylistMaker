package com.example.playlistmaker.presentation

import java.text.SimpleDateFormat
import java.util.Locale

object Formater {

    const val timePattern = "mm:ss"

    fun formayMillsTimeToDuration(millsTima: Long): String = SimpleDateFormat(
        timePattern, Locale.getDefault()
    ).format(millsTima)
}