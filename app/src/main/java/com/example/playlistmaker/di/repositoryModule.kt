package com.example.playlistmaker.di

import com.example.playlistmaker.history.data.HistoryRepository
import com.example.playlistmaker.history.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker.library.data.api.FavoriteTracksRepository
import com.example.playlistmaker.library.data.api.PlaylistRepository
import com.example.playlistmaker.util.TrackDbMapper
import com.example.playlistmaker.library.data.impl.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.library.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.player.data.AudioPlayerRepositoryImpl
import com.example.playlistmaker.search.data.TrackRepository
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.sharing.ExternalNavigator
import com.example.playlistmaker.settings.data.sharing.impl.ExternalNavigationImpl
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
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

    factory { TrackDbMapper() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

}