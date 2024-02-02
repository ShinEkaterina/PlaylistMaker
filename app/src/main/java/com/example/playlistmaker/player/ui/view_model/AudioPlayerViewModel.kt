package com.example.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioPlayerViewModel(
    private val audioPlayerInterator: AudioPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var statePlayerLiveData = MutableLiveData(PlayerState.STATE_PREPARED)
    private var timerJob: Job? = null

    private val _playlistState = MutableLiveData<PlaylistState>()
    val playlistState: LiveData<PlaylistState> = _playlistState

    private var currentTimerLiveData = MutableLiveData<Int>(0)
    private var isFavorite: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _showToast = SingleLiveEvent<String?>()
    val showToast: LiveData<String?> = _showToast

    fun getCurrentTimerLiveData(): LiveData<Int> = currentTimerLiveData
    fun getStatePlayerLiveData(): LiveData<PlayerState> = statePlayerLiveData

    fun getIsFavorite(): LiveData<Boolean> = isFavorite

    private fun renderFavorite(favorite: Boolean) {
        isFavorite.postValue(favorite)
    }

    fun preparePlayer(url: String) {
        audioPlayerInterator.preparePlayer(url) { state ->
            when (state) {
                PlayerState.STATE_PREPARED, PlayerState.STATE_DEFAULT -> {
                    statePlayerLiveData.postValue(PlayerState.STATE_PREPARED)
                    currentTimerLiveData.postValue(0)

                }

                else -> Unit
            }
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayerInterator.isPlaying()) {
                delay(DELAY_UPDATE_TIMER_MC)
                currentTimerLiveData.postValue(audioPlayerInterator.currentPosition())
            }
            Log.d("MY LOG", "set 00")
            currentTimerLiveData.postValue(0)
        }
    }

    fun changePlayerState() {
        audioPlayerInterator.switchPlayer { state ->
            when (state) {
                PlayerState.STATE_PLAYING -> {
                    startTimer()
                    statePlayerLiveData.postValue(PlayerState.STATE_PLAYING)
                }

                PlayerState.STATE_PAUSED -> {
                    timerJob?.cancel()
                    statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)
                }

                PlayerState.STATE_PREPARED -> {
                    statePlayerLiveData.postValue(PlayerState.STATE_PREPARED)
                    currentTimerLiveData.postValue(0)

                }

                else -> Unit
            }
        }
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            val favorite = isFavorite.value ?: false

            if (favorite) {
                favoriteTracksInteractor.delete(track)
                isFavorite.postValue(track.isFavorite)
            } else {
                favoriteTracksInteractor.add(track)
                isFavorite.postValue(track.isFavorite)
            }
            renderFavorite(!favorite)
        }
    }

    fun onPause() {
        statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)
        audioPlayerInterator.pausePlayer()
    }

    fun onResume() {
        statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)
    }

    fun updateLikeButton(trackId: Long) {
        viewModelScope.launch {
            favoriteTracksInteractor.checkFavorite(trackId).collect {
                renderFavorite(it)
            }
        }
    }

    fun getAllPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylist().collect { playlists ->
                renderState(playlists)
            }
        }
    }

    private fun renderState(playlists: List<Playlist>) {
        if (playlists.isNullOrEmpty())
            _playlistState.postValue(PlaylistState.Empty)
        else
            _playlistState.postValue(PlaylistState.Content(playlists))
    }

    fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            if (playlist.tracks?.contains(track.trackId.toString()) == true)
                _showToast.setValue("Трек уже добавлен в плейлист ${playlist.name}")
            else {
                playlistInteractor.addTrackToPlaylist(playlist, track)
                _showToast.setValue("Добавлено в плейлист ${playlist.name}")
            }
        }
    }

    companion object {
        const val DELAY_UPDATE_TIMER_MC = 300L
    }
}