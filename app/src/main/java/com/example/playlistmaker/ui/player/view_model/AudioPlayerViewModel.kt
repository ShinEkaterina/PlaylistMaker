package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.player.api.AudioPlayerInteractor


class AudioPlayerViewModel(
    private val audioPlayerInterator:AudioPlayerInteractor
) : ViewModel() {

    private var mainThreadHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = createUpdateTimerTask()
    private var statePlayerLiveData = MutableLiveData(PlayerState.STATE_PREPARED)

    fun getStatePlayerLiveData(): LiveData<PlayerState> = statePlayerLiveData

    private var currentTimerLiveData = MutableLiveData<Int>(0)

    fun getCurrentTimerLiveData(): LiveData<Int> = currentTimerLiveData


    fun preparePlayer(url: String) {
        audioPlayerInterator.preparePlayer(url) { state ->
            when (state) {
                PlayerState.STATE_PREPARED, PlayerState.STATE_DEFAULT -> {
                    statePlayerLiveData.postValue(PlayerState.STATE_PREPARED)
                    mainThreadHandler.removeCallbacks(timerRunnable)
                }

                else -> Unit
            }
        }
    }

    fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val currentTimerPosition = audioPlayerInterator.currentPosition()
                mainThreadHandler.postDelayed(this, DELAY_UPDATE_TIMER_MC)
                currentTimerLiveData.postValue(currentTimerPosition)
            }

        }
    }


    fun changePlayerState() {
        audioPlayerInterator.switchPlayer { state ->
            when (state) {
                PlayerState.STATE_PLAYING -> {
                    mainThreadHandler.removeCallbacks(timerRunnable)
                    mainThreadHandler.post(timerRunnable)
                    statePlayerLiveData.postValue(PlayerState.STATE_PLAYING)
                }

                PlayerState.STATE_PAUSED -> {
                    mainThreadHandler.removeCallbacks(timerRunnable)
                    statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)
                }

                PlayerState.STATE_PREPARED -> {
                    mainThreadHandler.removeCallbacks(timerRunnable)
                    mainThreadHandler.post(timerRunnable)
                    statePlayerLiveData.postValue(PlayerState.STATE_PREPARED)
                }

                else -> Unit
            }
        }
    }

    fun onPause() {
        mainThreadHandler.removeCallbacks(timerRunnable)
        statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)
        audioPlayerInterator.pausePlayer()

    }

    fun onDestroy() {
        mainThreadHandler.removeCallbacks(timerRunnable)

    }

    fun onResume() {
        mainThreadHandler.removeCallbacks(timerRunnable)
        statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)

    }

    companion object {
        const val DELAY_UPDATE_TIMER_MC = 300L
    }
}