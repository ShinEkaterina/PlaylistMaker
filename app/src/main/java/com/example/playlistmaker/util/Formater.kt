package com.example.playlistmaker.util

import java.text.SimpleDateFormat
import java.util.Locale

object Formater {

    const val timePattern = "mm:ss"

    fun formayMillsTimeToDuration(millsTima: Long): String = SimpleDateFormat(
        timePattern, Locale.getDefault()
    ).format(millsTima)

    fun formayMillsTimeToDuration(millsTima: Int): String = SimpleDateFormat(
        timePattern, Locale.getDefault()
    ).format(millsTima)
}