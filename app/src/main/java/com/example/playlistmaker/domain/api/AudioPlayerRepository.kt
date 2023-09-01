package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface AudioPlayerRepository {
    fun getAudioPlayer(track: Track): Player
}