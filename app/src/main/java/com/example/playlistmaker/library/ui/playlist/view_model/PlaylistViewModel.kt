package com.example.playlistmaker.library.ui.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.stream.Collectors


class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlistTracks = MutableLiveData<PlaylistTracksState>()
    val playlistTracks: LiveData<PlaylistTracksState> = _playlistTracks

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

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
        if (tracks.isEmpty())
            _playlistTracks.postValue(PlaylistTracksState.Empty)
        else
            _playlistTracks.postValue(PlaylistTracksState.Content(tracks))
    }

    fun deleteTrackFromPlaylist(playlistId: Long, track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(playlistId, track)
            getTracks(playlistId)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
        }
    }

    fun getTracksInfo(tracks: List<Track>): String {
        val tracksInfo = tracks.mapIndexed { index, track ->
            "${index + 1}.${track.artistName} - ${track.trackName}(${
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(track.trackTimeMillis)
            })"
        }
        return tracksInfo.stream().collect(Collectors.joining("\n"))
    }
}