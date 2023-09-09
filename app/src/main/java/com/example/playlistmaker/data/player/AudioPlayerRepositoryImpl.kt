package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository
import com.example.playlistmaker.domain.model.Track

class AudioPlayerRepositoryImpl() : AudioPlayerRepository {

    private val mediaPlayer: MediaPlayer = MediaPlayer()


    override fun prepare(url: String,prepare: () -> Unit, onComplete: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { prepare() }
        mediaPlayer.setOnCompletionListener {
            onComplete()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun destroy() {
        mediaPlayer.release()
    }


    override fun getCurrentTime(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

}