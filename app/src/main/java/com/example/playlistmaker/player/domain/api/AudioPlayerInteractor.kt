package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.model.PlayerState

interface AudioPlayerInteractor {
    fun preparePlayer(url: String, onStateChanged: (s: PlayerState) -> Unit)
    fun pausePlayer()
    fun currentPosition(): Int
    fun switchPlayer(onStateChangedTo: (s: PlayerState) -> Unit)

    fun isPlaying(): Boolean


}