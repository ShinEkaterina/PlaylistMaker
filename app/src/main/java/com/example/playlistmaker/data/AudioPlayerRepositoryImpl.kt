package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.AudioPlayerRepository
import com.example.playlistmaker.domain.api.Player
import com.example.playlistmaker.domain.model.Track

class AudioPlayerRepositoryImpl:AudioPlayerRepository {
    override fun getAudioPlayer(track: Track): Player {
        return AudioPlayer(track)
    }

}