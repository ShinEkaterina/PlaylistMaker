package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepository
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl

object Creator {
    private fun provideAudioPlayerRepository(track: Track): AudioPlayerRepository = AudioPlayerRepositoryImpl(track)

    fun provideAudioPlayerInteractor(track: Track): AudioPlayerInteractorImpl {
        val rep = provideAudioPlayerRepository(track)
        return AudioPlayerInteractorImpl(rep, track)
    }

    private fun getTracksRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }
}

