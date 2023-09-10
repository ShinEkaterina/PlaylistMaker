package com.example.playlistmaker.domain.history

import com.example.playlistmaker.domain.model.Track

interface HistoryInteractor {

    fun addTrackToHistory(track: Track)

    fun getHistoryString(): String?

    fun clearHistory()

    fun updateHistory(updatedHistoryList: ArrayList<Track>)

    fun getHistoryList(): ArrayList<Track>
}