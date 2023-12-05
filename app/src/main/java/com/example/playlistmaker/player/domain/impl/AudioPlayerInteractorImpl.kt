package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.playlistmaker.player.domain.model.PlayerState


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

    override fun isPlaying(): Boolean {
        return mediaPlayerRepository.isPlaying()
    }

}