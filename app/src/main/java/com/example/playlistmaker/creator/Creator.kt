package com.example.playlistmaker.creator

import com.example.playlistmaker.data.AudioPlayerRepositoryImpl
import com.example.playlistmaker.domain.api.AudioPlayerRepository
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.model.Track

object Creator {
    private fun provideAudioPlayerRepository(): AudioPlayerRepository = AudioPlayerRepositoryImpl()

    fun provideAudioPlayerInteractor(track:Track) :AudioPlayerInteractorImpl{
        val rep = provideAudioPlayerRepository()
        return AudioPlayerInteractorImpl(rep, track)
    }
}