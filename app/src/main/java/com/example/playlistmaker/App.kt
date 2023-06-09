package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
const val SHARED_PREFERENCES = "shared_preferences"
const val DARK_THEME_KEY = "dark_theme_key"

class App : Application() {

    var darkTheme = false


    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, false)
        sharedPreferences.edit()
            .putBoolean("TYFYT", true)
            .apply()
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
