package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository
import com.example.playlistmaker.domain.model.PlayerState


class AudioPlayerInteractorImpl(
    private var audioPlayerRepository: AudioPlayerRepository
) : AudioPlayerInteractor {

    private var currentState: PlayerState = PlayerState.STATE_DEFAULT


    override fun preparePlayer(url: String,prepare: () -> Unit, onComplete: () -> Unit) {
        audioPlayerRepository.prepare(
            url,
            prepare,
            onComplete
        )
        currentState = PlayerState.STATE_PREPARED
        prepare()
    }

    override fun getPlayerState(): PlayerState = currentState


    override fun setPlayerState(state: PlayerState) {
        currentState = state
    }

    override fun playMusic() {
        audioPlayerRepository.start()
        currentState = PlayerState.STATE_PLAYING
    }

    override fun pauseMusic() {
        audioPlayerRepository.pause()
        currentState = PlayerState.STATE_PAUSED
    }

    override fun destroyPlayer() {
        audioPlayerRepository.destroy()
    }

    override fun getCurrentTime() = audioPlayerRepository.getCurrentTime()


    override fun switchPlayer(onStateChangedTo: (s: PlayerState) -> Unit) {
        when (currentState) {
            PlayerState.STATE_DEFAULT -> {}
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED  -> {
                playMusic()
                onStateChangedTo(PlayerState.STATE_PLAYING)
            }
            PlayerState.STATE_PLAYING -> {
                pauseMusic()
                onStateChangedTo(PlayerState.STATE_PAUSED)
            }

        }
    }

}