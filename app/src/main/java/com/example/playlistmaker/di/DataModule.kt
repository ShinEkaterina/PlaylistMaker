package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.history.data.HistoryRepository
import com.example.playlistmaker.history.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.iTunesApi
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
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

    factory(named(SHARED_PREFS_SEARCH_HISTORY)) {
        androidContext()
            .getSharedPreferences(SHARED_PREFS_SEARCH_HISTORY, Context.MODE_PRIVATE)
    }

    factory(named(SHARED_PREFS_DARK_MODE)) {
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
// Database
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
    }
}