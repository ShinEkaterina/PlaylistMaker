package com.example.playlistmaker.ui.main.view_model


import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.navigation.InternalNavigationInteractor

class MainViewModel(private val internalNavigationInteractor: InternalNavigationInteractor) :
    ViewModel() {

    fun toSettingsScreen() {
        internalNavigationInteractor.toSettingsScreen()
    }

    fun toLibraryScreen() {
        internalNavigationInteractor.toLibraryScreen()
    }

    fun toSearchScreen() {
        internalNavigationInteractor.toSearchScreen()
    }

}