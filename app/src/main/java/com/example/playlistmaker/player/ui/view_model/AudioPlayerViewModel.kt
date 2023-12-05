package com.example.playlistmaker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AudioPlayerViewModel(
    private val audioPlayerInterator: AudioPlayerInteractor
) : ViewModel() {

    private var statePlayerLiveData = MutableLiveData(PlayerState.STATE_PREPARED)
    private var timerJob: Job? = null

    fun getStatePlayerLiveData(): LiveData<PlayerState> = statePlayerLiveData

    private var currentTimerLiveData = MutableLiveData<Int>(0)

    fun getCurrentTimerLiveData(): LiveData<Int> = currentTimerLiveData

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
            Log.d("MY LOG","set 00")
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