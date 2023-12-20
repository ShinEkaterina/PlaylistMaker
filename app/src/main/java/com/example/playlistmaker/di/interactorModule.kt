package com.example.playlistmaker.di

import com.example.playlistmaker.history.domain.HistoryInteractor
import com.example.playlistmaker.history.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.library.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.library.domain.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SharingInteractor
import com.example.playlistmaker.settings.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }


    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

   single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }



}