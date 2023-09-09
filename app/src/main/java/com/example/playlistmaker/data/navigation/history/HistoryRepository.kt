package com.example.playlistmaker.data.navigation.history

interface HistoryRepository {

    fun updateTrackHistory(tracksHistory: String)
    fun getHistoryString() : String
    fun clearHistory(): Unit
}