package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository


class AudioPlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer
) : AudioPlayerRepository {

    private var playerState = PlayerState.STATE_PREPARED

    override fun preparePlayer(
        url: String,
        onStateChangedTo: (s: PlayerState) -> Unit
    ) {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            onStateChangedTo(PlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            onStateChangedTo(PlayerState.STATE_PREPARED)

        }
        this.mediaPlayer = mediaPlayer

    }


    override fun pause() {
        this.mediaPlayer.pause()
        playerState = PlayerState.STATE_PREPARED
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun switchPlayerState(onStateChangedTo: (s: PlayerState) -> Unit) {
        when (playerState) {
            PlayerState.STATE_DEFAULT -> {}

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                mediaPlayer.start()
                playerState = PlayerState.STATE_PLAYING
                onStateChangedTo(PlayerState.STATE_PLAYING)

            }

            PlayerState.STATE_PLAYING -> {
                mediaPlayer.pause()
                playerState = PlayerState.STATE_PAUSED
                onStateChangedTo(PlayerState.STATE_PAUSED)

            }
        }
    }


    override fun exit() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}