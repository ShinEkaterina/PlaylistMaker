package com.example.playlistmaker.data.settings.impl

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

//const val SHARED_PREFERENCES = "shared_preferences"
const val DARK_THEME_KEY = "dark_theme_key"

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository {

/*    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)*/

    companion object {
        private val map = ThemeSettings.values().associateBy(ThemeSettings::intMode)
        fun fromBooltoThemeState(type: Boolean) = map[type]
    }

    override fun getThemeSettings(): ThemeSettings {
        val intMode = sharedPreferences.getBoolean(DARK_THEME_KEY, false)
        return fromBooltoThemeState(intMode) ?: ThemeSettings.MODE_DARK_NO
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, settings.intMode).apply()
    }
}