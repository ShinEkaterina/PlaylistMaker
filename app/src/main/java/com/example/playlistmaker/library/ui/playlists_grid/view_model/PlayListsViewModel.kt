package com.example.playlistmaker.library.ui.playlists_grid.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val _libraryPlaylist = MutableLiveData<PlaylistsFragmentState>()
    val libraryPlaylist: LiveData<PlaylistsFragmentState> = _libraryPlaylist

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylist().collect { playlists ->
                renderState(playlists)
            }
        }
    }

    private fun renderState(playlists: List<Playlist>) {
        if (playlists.isNullOrEmpty())
            _libraryPlaylist.postValue(PlaylistsFragmentState.Empty)
        else
            _libraryPlaylist.postValue(PlaylistsFragmentState.Content(playlists))
    }
}

