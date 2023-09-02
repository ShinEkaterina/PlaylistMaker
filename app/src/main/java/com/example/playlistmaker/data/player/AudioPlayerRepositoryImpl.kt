package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository
import com.example.playlistmaker.domain.model.Track

class AudioPlayerRepositoryImpl(track:Track) : AudioPlayerRepository {

    private val mediaPlayer: MediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun prepare(prepare: () -> Unit, onComplete: () -> Unit) {
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