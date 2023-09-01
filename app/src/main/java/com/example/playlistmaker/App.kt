package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val SHARED_PREFERENCES = "shared_preferences"
const val DARK_THEME_KEY = "dark_theme_key"

class App : Application() {

    var darkTheme = false

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        const val SHARED_PREFERENCES = "shared_preferences"
        const val DARK_THEME_KEY = "DARK_THEME_KEY"
        const val KEY_FOR_HISTORY_LIST = "KEY_FOR_HISTORY_LIST"
        const val TRACK = "track"
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

        )
    }
}
