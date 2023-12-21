package com.example.playlistmaker.di


import com.example.playlistmaker.library.ui.view_model.tracks.FavoriteTracksViewModel
import com.example.playlistmaker.library.ui.view_model.playlist.LibraryPlayListsViewModel
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {


    viewModel {
        AudioPlayerViewModel(get(), get ())
    }

    viewModel {
        SettingsViewModel(get(),get())
    }

    viewModel {
        SearchViewModel(get(),get())
    }

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel {
        LibraryPlayListsViewModel()
    }
}