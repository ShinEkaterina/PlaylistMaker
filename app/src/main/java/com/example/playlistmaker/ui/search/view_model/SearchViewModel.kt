package com.example.playlistmaker.ui.search.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.data.search.network.ErrorCode
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.presentation.player.model.ErrorType
import com.example.playlistmaker.presentation.player.model.SearchScreenState
import com.example.playlistmaker.util.Creator

class SearchViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val searchInteractor = Creator.provideTracksInteractor(getApplication<Application>())
    private val historyInteractor = Creator.provideHistoryInteractor(getApplication<Application>())
    private val internalNavigationInteractor =
        Creator.provideNavigationInteractor(getApplication<Application>())


    private var searchTrackStatusLiveData = MutableLiveData<SearchScreenState>()

    fun getSearchTrackStatusLiveData(): LiveData<SearchScreenState> = searchTrackStatusLiveData


    private var trackList = ArrayList<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText
        if (newSearchText!!.isEmpty()) {
            getHistory()
            showHistory()
        } else {
            searchAction(newSearchText)
        }
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    fun onResume() {
        getHistory()
        if (trackList.isEmpty()) {
            showHistory()
        }
    }

    // поиск по вводу каждые 2 сек
    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun getHistory(): ArrayList<Track> {
        return historyInteractor.getHistoryList()
    }

    fun showHistory() {
        searchTrackStatusLiveData.postValue(
            SearchScreenState(
                emptyList(),
                false,
                errorType = null,
                toShowHistory = true,
                history = getHistory(),
            )
        )
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
    }

    fun addNewTrackToHistory(track: Track) {
        historyInteractor.addTrackToHistory(track)
    }

    fun openTrackAudioPlayer(track: Track) {
        internalNavigationInteractor.openTrack(track)
    }

    fun searchAction(newSearchText: String) {

        if (newSearchText.isNotEmpty()) {
            searchTrackStatusLiveData.postValue(
                SearchScreenState(
                    trackList,
                    true,
                    null,
                    toShowHistory = false,
                    history = emptyList(),
                )
            )
        }

        searchInteractor.searchTracks(newSearchText, object : TracksInteractor.TrackConsumer {
            override fun consume(
                foundTracks: List<Track>?,
                errorCode: ErrorCode?,
                errorMessage: String?
            ) {
                handler.post {
                    if (foundTracks != null) {
                        trackList.clear()
                        trackList.addAll(foundTracks)

                    }
                    if (errorCode != null) {
                        when (errorCode) {
                            ErrorCode.NO_INTERNET -> {
                                searchTrackStatusLiveData.postValue(
                                    SearchScreenState(
                                        emptyList(),
                                        false,
                                        ErrorType.NO_INTERNET,
                                        toShowHistory = false,
                                        history = emptyList(),
                                    )
                                )
                            }

                            ErrorCode.UNKNOWN_ERROR -> {
                                searchTrackStatusLiveData.postValue(
                                    SearchScreenState(
                                        emptyList(),
                                        false,
                                        ErrorType.NO_INTERNET,
                                        toShowHistory = false,
                                        history = emptyList(),
                                    )
                                )
                            }

                            ErrorCode.NOTHING_FOUND -> {
                                searchTrackStatusLiveData.postValue(
                                    SearchScreenState(
                                        emptyList(),
                                        false,
                                        ErrorType.NOTHINF_FOUND,
                                        toShowHistory = false,
                                        history = emptyList(),
                                    )
                                )
                            }

                        }
                    } else {
                        searchTrackStatusLiveData.postValue(
                            SearchScreenState(
                                trackList,
                                false,
                                null,
                                toShowHistory = false,
                                history = emptyList(),
                            )
                        )
                    }


                }
            }
        })
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(this[APPLICATION_KEY] as Application)
                }
            }
    }

}