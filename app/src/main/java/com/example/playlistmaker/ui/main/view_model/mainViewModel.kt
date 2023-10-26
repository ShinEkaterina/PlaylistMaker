package com.example.playlistmaker.ui.main.view_model


import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.navigation.InternalNavigationInteractor

class MainViewModel(private val internalNavigationInteractor: InternalNavigationInteractor) :
    ViewModel() {

/*    fun goToSettingsScreen() {
        internalNavigationInteractor.toSettingsScreen()
    }*/

    fun goToLibraryScreen() {
        internalNavigationInteractor.toLibraryScreen()
    }

/*    fun goToSearchScreen() {
        internalNavigationInteractor.toSearchScreen()
    }*/

}