package com.example.playlistmaker.history.domain

import com.example.playlistmaker.common.domain.model.Track

interface HistoryInteractor {

    fun addTrackToHistory(track: Track)

    fun getHistoryString(): String?

    fun clearHistory()

    fun updateHistory(updatedHistoryList: ArrayList<Track>)

    fun getHistoryList(): ArrayList<Track>
}