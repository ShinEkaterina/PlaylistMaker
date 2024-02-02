package com.example.playlistmaker.di


import com.example.playlistmaker.common.presentation.ConfirmationDialog
import com.example.playlistmaker.library.ui.playlists.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.library.ui.tracks.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.playlistmaker.library.ui.playlists.view_model.PlaylistsViewModel
import com.example.playlistmaker.library.ui.playlist.view_model.PlaylistViewModel


val viewModelModule = module {


    viewModel {
        AudioPlayerViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel {
        PlaylistCreateViewModel(androidContext(), get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    single { ConfirmationDialog(get()) }


    viewModel {
        PlaylistViewModel(androidContext(), get(), get())
    }
}