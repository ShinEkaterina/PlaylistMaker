package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository
import com.example.playlistmaker.domain.model.PlayerState


class AudioPlayerInteractorImpl(
    private val mediaPlayerRepository: AudioPlayerRepository
) : AudioPlayerInteractor {

    override fun preparePlayer(url: String, onStateChanged: (s: PlayerState) -> Unit) {
        mediaPlayerRepository.preparePlayer(
            url,
            onStateChanged
        )
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pause()
    }

    override fun currentPosition(): Int {
        return mediaPlayerRepository.currentPosition()
    }

    override fun switchPlayer(onStateChangedTo: (s: PlayerState) -> Unit) {
        mediaPlayerRepository.switchPlayerState(onStateChangedTo)

    }

}