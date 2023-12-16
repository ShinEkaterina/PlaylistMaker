package com.example.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioPlayerViewModel(
    private val audioPlayerInterator: AudioPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private var statePlayerLiveData = MutableLiveData(PlayerState.STATE_PREPARED)
    private var timerJob: Job? = null



    private var currentTimerLiveData = MutableLiveData<Int>(0)
    private var isFavorite: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getCurrentTimerLiveData(): LiveData<Int> = currentTimerLiveData
    fun getStatePlayerLiveData(): LiveData<PlayerState> = statePlayerLiveData

    fun getIsFavorite(): LiveData<Boolean> = isFavorite

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
            if (track.isFavorite){
                favoriteTracksInteractor.delete(track)
                track.isFavorite = false
                isFavorite.postValue(track.isFavorite)
            }else{
                favoriteTracksInteractor.add(track)
                track.isFavorite = true
                isFavorite.postValue(track.isFavorite)
            }
        }
    }

    fun onPause() {
        statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)
        audioPlayerInterator.pausePlayer()

    }


    fun onResume() {
        statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)

    }

    companion object {
        const val DELAY_UPDATE_TIMER_MC = 300L
    }
}