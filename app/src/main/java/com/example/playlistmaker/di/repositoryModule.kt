package com.example.playlistmaker.di

import com.example.playlistmaker.data.history.HistoryRepository
import com.example.playlistmaker.data.history.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepository
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigationImpl
import com.example.playlistmaker.domain.player.api.AudioPlayerRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }


    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigationImpl(androidContext())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(named(SHARED_PREFS_DARK_MODE)))
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(named(SHARED_PREFS_SEARCH_HISTORY)))
    }

}