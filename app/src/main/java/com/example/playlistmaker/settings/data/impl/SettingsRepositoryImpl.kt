package com.example.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

const val DARK_THEME_KEY = "dark_theme_key"

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        val intMode = sharedPreferences.getBoolean(DARK_THEME_KEY, false)
        return fromBooltoThemeState(intMode) ?: ThemeSettings.MODE_DARK_NO
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, settings.intMode).apply()
    }

    companion object {
        private val map = ThemeSettings.values().associateBy(ThemeSettings::intMode)
        fun fromBooltoThemeState(type: Boolean) = map[type]
    }
}