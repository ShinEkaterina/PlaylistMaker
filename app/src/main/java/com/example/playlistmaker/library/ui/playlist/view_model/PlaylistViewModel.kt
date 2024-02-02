package com.example.playlistmaker.library.ui.playlist.view_model

import PLAYLIST_STORAGE_NAME
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.common.presentation.ConfirmationDialog
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class PlaylistViewModel(
    context: Context,
    private val playlistInteractor: PlaylistInteractor,
    private val confirmator: ConfirmationDialog
) : ViewModel() {

    private val _playlistTracks = MutableLiveData<PlaylistTracksState>()
    val playlistTracks: LiveData<PlaylistTracksState> = _playlistTracks

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    private val filePath by lazy {
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), PLAYLIST_STORAGE_NAME)
    }

    fun updatePlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylist(playlistId).collect(::updatePlaylist)
        }
    }

    private fun updatePlaylist(playlist: Playlist) {
        _playlist.postValue(playlist)
    }

    fun getTracks(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.getTracksPlaylist(playlistId).collect(::renderState)
        }
    }

    private fun renderState(tracks: List<Track>) {
        if (tracks.isNullOrEmpty())
            _playlistTracks.postValue(PlaylistTracksState.Empty)
        else
            _playlistTracks.postValue(PlaylistTracksState.Content(tracks))
    }

    fun getImage(name: String?) = File(filePath, name).toUri()

    fun deleteTrackFromPlaylist(playlistId: Long, track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(playlistId, track)
            getTracks(playlistId)
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlistId)
        }
    }
}