package com.example.playlistmaker.domain.player.api

import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track


interface AudioPlayerRepository {

    fun preparePlayer(url: String, onStateChangedTo: (s: PlayerState) -> Unit)

    fun currentPosition(): Int

    fun pause()

    fun switchPlayerState(onStateChangedTo: (s: PlayerState) -> Unit)

    fun exit()

    fun isPlaying(): Boolean
}