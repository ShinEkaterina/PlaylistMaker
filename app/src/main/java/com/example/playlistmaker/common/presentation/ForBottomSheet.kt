package com.example.playlistmaker.common.presentation

import android.content.res.Resources

fun Resources.calculateDesiredHeight(offsetDp: Int, minHeightPx: Int = 100): Int {
    //Рассчет отступа в пикселях
    val totalOffsetPx = (offsetDp * displayMetrics.density).toInt()

    return (displayMetrics.heightPixels - (displayMetrics.widthPixels + totalOffsetPx))
        .coerceAtLeast(
            minHeightPx
        )
}