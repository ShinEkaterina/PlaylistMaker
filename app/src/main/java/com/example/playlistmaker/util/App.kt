package com.example.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule


class App : Application(), KoinComponent {

    override fun onCreate() {
        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                interactorModule,
                repositoryModule,
                viewModelModule
            )
        }
       val settingsInteractor = getKoin().get<SettingsInteractor>()

        switchTheme(settingsInteractor.getThemeSettings())
        super.onCreate()

    }

    fun switchTheme(isDarkMode: ThemeSettings) {
        if (isDarkMode == ThemeSettings.MODE_DARK_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }

    companion object {
        const val TRACK = "track"
    }
}
