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
import org.koin.android.ext.android.inject


class App : Application(), KoinComponent {

    /*    var darkTheme = ThemeSettings.MODE_DARK_NO*/
    private val settingsInteractor: SettingsInteractor by inject()

    companion object {
        const val TRACK = "track"
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                interactorModule,
                repositoryModule,
                viewModelModule
            )
        }

     //   val settingsInteractor = getKoin().get<SettingsInteractor>()

        switchTheme(settingsInteractor.getThemeSettings())
    }

    fun switchTheme(isDarkMode: ThemeSettings) {
        if (isDarkMode == ThemeSettings.MODE_DARK_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
}
