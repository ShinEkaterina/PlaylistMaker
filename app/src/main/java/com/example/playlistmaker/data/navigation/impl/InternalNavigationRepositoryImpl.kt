package com.example.playlistmaker.data.navigation.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.data.navigation.InternalNavigationRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.library.activity.LibraryActivity
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.util.App

class InternalNavigationRepositoryImpl(private val context: Context) :
    InternalNavigationRepository {
/*    override fun toSettingsScreen() {
        val displayIntent =
            Intent(context, SettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(displayIntent)
    }*/

    override fun toLibraryScreen() {
        val displayIntent =
            Intent(context, LibraryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(displayIntent)
    }

    override fun toSearchScreen() {
        val displayIntent =
            Intent(context, SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(displayIntent)
    }

    override fun openTrack(track: Track) {
        val displayIntent =
            Intent(context, AudioPlayerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        displayIntent.putExtra(App.TRACK, track)
        context.startActivity(displayIntent)
    }

}