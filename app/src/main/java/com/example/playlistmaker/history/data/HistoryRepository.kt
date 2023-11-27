package com.example.playlistmaker.history.data

interface HistoryRepository {

    fun updateTrackHistory(updatedHistory: String)
    fun getHistoryString(): String
    fun clearHistory()
}