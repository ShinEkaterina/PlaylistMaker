package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.history.HistoryRepository
import com.example.playlistmaker.data.history.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.iTunesApi
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val ITUNES_BASE_URL = "https://itunes.apple.com"
const val SHARED_PREFS_DARK_MODE = "shared_prefs_dark_mode"
const val SHARED_PREFS_SEARCH_HISTORY = "search_history"
const val LOCAL_STORAGE = "local_storage"



val dataModule = module {

    single<iTunesApi> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApi::class.java)

    }

    factory (named(SHARED_PREFS_SEARCH_HISTORY)){
        androidContext()
            .getSharedPreferences(SHARED_PREFS_SEARCH_HISTORY, Context.MODE_PRIVATE)
    }

    factory (named(SHARED_PREFS_DARK_MODE)){
        androidContext()
            .getSharedPreferences(SHARED_PREFS_DARK_MODE, Context.MODE_PRIVATE)
    }

    single {
        androidContext()
            .getSharedPreferences(LOCAL_STORAGE, Context.MODE_PRIVATE)
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }


    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
    factory { Gson() }

    single {
        return@single MediaPlayer()
    }
}