package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.model.PlayerState


interface AudioPlayerRepository {

    fun preparePlayer(url: String, onStateChangedTo: (s: PlayerState) -> Unit)

    fun currentPosition(): Int

    fun pause()

    fun switchPlayerState(onStateChangedTo: (s: PlayerState) -> Unit)

    fun exit()

    fun isPlaying(): Boolean
}