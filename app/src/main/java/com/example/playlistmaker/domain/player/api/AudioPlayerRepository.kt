package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.model.Track

interface AudioPlayerRepository {

    fun prepare(prepare: () -> Unit, onComplete: () -> Unit)
    fun start()
    fun pause()
    fun destroy()
    fun getCurrentTime(): Long
}