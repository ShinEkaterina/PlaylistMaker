package com.example.playlistmaker.data.history

interface HistoryRepository {

    fun updateTrackHistory(updatedHistory: String)
    fun getHistoryString(): String
    fun clearHistory()
}