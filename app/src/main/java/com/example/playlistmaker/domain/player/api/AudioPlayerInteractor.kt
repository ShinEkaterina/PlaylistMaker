package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.model.PlayerState

interface AudioPlayerInteractor {
    fun preparePlayer(url: String, onStateChanged: (s: PlayerState) -> Unit)
    fun pausePlayer()
    fun currentPosition(): Int
    fun switchPlayer(onStateChangedTo: (s: PlayerState) -> Unit)


}