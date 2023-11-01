package com.example.playlistmaker.di


import com.example.playlistmaker.ui.library.view_model.LibraryPlayListsViewModel
import com.example.playlistmaker.ui.library.view_model.LibraryTracksFragmentViewModel
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {


    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(),get())
    }

    viewModel {
        SearchViewModel(get(),get())
    }

    viewModel {
        LibraryTracksFragmentViewModel()
    }

    viewModel {
        LibraryPlayListsViewModel()
    }
}