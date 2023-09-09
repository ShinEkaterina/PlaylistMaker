package com.example.playlistmaker.domain.navigation.impl

import com.example.playlistmaker.data.navigation.InternalNavigationRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.navigation.InternalNavigationInteractor

class InternalNavigationInteractorImpl (private val internalNavigationRepository: InternalNavigationRepository) :
    InternalNavigationInteractor {
    override fun toSettingsScreen() {
        internalNavigationRepository.toSettingsScreen()
    }

    override fun toLibraryScreen() {
        internalNavigationRepository.toLibraryScreen()
    }

    override fun toSearchScreen() {
        internalNavigationRepository.toSearchScreen()
    }

    override fun openTrack(track: Track) {
        internalNavigationRepository.openTrack(track)
    }

}