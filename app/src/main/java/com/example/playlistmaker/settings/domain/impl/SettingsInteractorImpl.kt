package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(
    private val settingRepository: SettingsRepository
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingRepository.updateThemeSetting(settings)
    }
}