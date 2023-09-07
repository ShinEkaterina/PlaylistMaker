package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsInteractorImpl(  private val settingRepository: SettingsRepository
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingRepository.updateThemeSetting(settings)
    }
}