package com.example.playlistmaker.data.navigation.history

interface HistoryRepository {

    fun updateTrackHistory(updatedHistory: String)
    fun getHistoryString(): String
    fun clearHistory()
}