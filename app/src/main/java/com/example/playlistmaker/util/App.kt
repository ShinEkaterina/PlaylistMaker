package com.example.playlistmaker.util

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.model.ThemeSettings


class App : Application() {

    var darkTheme = ThemeSettings.MODE_DARK_NO

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        const val TRACK = "track"
    }

    override fun onCreate() {
        super.onCreate()
        val settingsInteractor = Creator.provideSettingInteractor(applicationContext)
        switchTheme(settingsInteractor.getThemeSettings())
    }

    fun switchTheme(darkThemeEnabled: ThemeSettings) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            when (darkTheme) {
                ThemeSettings.MODE_DARK_NO -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeSettings.MODE_DARK_YES -> AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }
}
