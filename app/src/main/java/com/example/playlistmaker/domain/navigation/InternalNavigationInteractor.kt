package com.example.playlistmaker.domain.navigation

import com.example.playlistmaker.domain.model.Track

interface InternalNavigationInteractor {

   // fun toSettingsScreen()

   // fun toLibraryScreen()

   // fun toSearchScreen()

    fun openTrack(track: Track)

}