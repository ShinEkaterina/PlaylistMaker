package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepository
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigationImpl
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {
    private fun provideAudioPlayerRepository(track: Track): AudioPlayerRepository = AudioPlayerRepositoryImpl(track)

    fun provideAudioPlayerInteractor(track: Track): AudioPlayerInteractorImpl {
        val rep = provideAudioPlayerRepository(track)
        return AudioPlayerInteractorImpl(rep, track)
    }

    private fun getTracksRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }
    fun provideSettingInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingRepository(context))
    }
    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository(context))
    }
    fun getSettingRepository(context: Context): SettingsRepositoryImpl {
        return SettingsRepositoryImpl(context)
    }
    fun getSharingRepository(context: Context): ExternalNavigationImpl {
        return ExternalNavigationImpl(context)
    }
}
