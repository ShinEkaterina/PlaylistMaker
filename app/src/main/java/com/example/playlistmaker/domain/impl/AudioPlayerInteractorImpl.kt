package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.AudioPlayerRepository
import com.example.playlistmaker.domain.api.Player
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track


class AudioPlayerInteractorImpl(
    private var rep: AudioPlayerRepository, private val track: Track
) : AudioPlayerInteractor {

    private val mediaPlayer: Player = rep.getAudioPlayer(track)
    private var currentState: PlayerState = PlayerState.STATE_DEFAULT


    override fun preparePlayer(prepare: () -> Unit, onComplete: () -> Unit) {
        mediaPlayer.prepare(prepare, onComplete)
        currentState = PlayerState.STATE_PREPARED
    }

    override fun getPlayerState(): PlayerState = currentState


    override fun setPlayerState(state: PlayerState) {
        currentState = state
    }

    override fun playMusic() {
        mediaPlayer.start()
        currentState = PlayerState.STATE_PLAYING
    }

    override fun pauseMusic() {
        mediaPlayer.pause()
        currentState = PlayerState.STATE_PAUSED
    }

    override fun destroyPlayer() {
        mediaPlayer.destroy()
    }

    override fun getCurrentTime() = mediaPlayer.getCurrentTime()

    override fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit) {
        when (currentState) {
            PlayerState.STATE_PLAYING -> {
                onPausePlayer()
                pauseMusic()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_DEFAULT -> {
                onStartPlayer()
                playMusic()
            }
        }
    }


}