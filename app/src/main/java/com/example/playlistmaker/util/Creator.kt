package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.data.navigation.history.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.navigation.impl.InternalNavigationRepositoryImpl
import com.example.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepository
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigationImpl
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.history.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.navigation.InternalNavigationInteractor
import com.example.playlistmaker.domain.navigation.impl.InternalNavigationInteractorImpl
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

/*
object Creator {
    private fun provideAudioPlayerRepository(): AudioPlayerRepository = AudioPlayerRepositoryImpl()

    fun provideAudioPlayerInteractor(): AudioPlayerInteractorImpl {
        val rep = provideAudioPlayerRepository()
        return AudioPlayerInteractorImpl(rep)
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

    fun provideNavigationInteractor(context: Context): InternalNavigationInteractor {
        return InternalNavigationInteractorImpl(InternalNavigationRepositoryImpl(context))
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(
            HistoryRepositoryImpl(context)
        )
    }

}

*/
