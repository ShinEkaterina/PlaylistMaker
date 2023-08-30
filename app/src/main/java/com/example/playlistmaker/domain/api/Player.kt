package com.example.playlistmaker.domain.api

interface Player {
    fun prepare(prepare: () -> Unit, onComplete: () -> Unit)
    fun start()
    fun pause()
    fun destroy()
    fun getCurrentTime(): Long
}