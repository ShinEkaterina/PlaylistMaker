package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.search.network.ErrorCode
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.presentation.player.model.ErrorType
import com.example.playlistmaker.presentation.player.model.SearchScreenState
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch


class SearchViewModel(
    private val searchInteractor: TracksInteractor, private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private var searchTrackStatusLiveData = MutableLiveData<SearchScreenState>()
    fun getSearchTrackStatusLiveData(): LiveData<SearchScreenState> = searchTrackStatusLiveData

    private var trackList = ArrayList<Track>()
    private var lastSearchText: String? = null

    private var onSearchDebounce: (Unit) -> Unit = debounce<Unit>(
        SEARCH_DEBOUNCE_DELAY_MILLISECONDS, viewModelScope, false
    ) {
        val newSearchText = lastSearchText
        if (newSearchText!!.isEmpty()) {
            getHistory()
            showHistory()
        } else {
            searchAction(newSearchText)
        }

    }

    fun onResume() {
        getHistory()
        if (trackList.isEmpty()) {
            showHistory()
        }
    }

    // поиск по вводу каждые 2 сек
    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) {
            return
        }
        lastSearchText = changedText
        onSearchDebounce(Unit)
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
        viewModelScope.launch {
            searchInteractor.searchTracks(newSearchText).collect { pair ->
                    val foundTracks = pair.first
                    val errorCode = pair.second
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
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLISECONDS = 2000L
    }

}