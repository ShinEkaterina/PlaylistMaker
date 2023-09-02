package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.model.PlayerState

interface AudioPlayerInteractor {
    fun playMusic()
    fun pauseMusic()
    fun destroyPlayer()
    fun getCurrentTime(): Long
    fun preparePlayer(prepare: () -> Unit, onComplete: () -> Unit)
    fun getPlayerState(): PlayerState
    fun setPlayerState(state: PlayerState)
    fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit)
}