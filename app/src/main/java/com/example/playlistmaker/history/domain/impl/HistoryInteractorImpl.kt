package com.example.playlistmaker.history.domain.impl

import com.example.playlistmaker.history.data.HistoryRepository
import com.example.playlistmaker.history.domain.HistoryInteractor
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.util.MAX_NUMBER_OF_TRACK_HISTORY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HistoryInteractorImpl(private val historyRepository: HistoryRepository) : HistoryInteractor {

    override fun getHistoryString(): String = historyRepository.getHistoryString()

    override fun getHistoryList(): ArrayList<Track> {
        if (getHistoryString().isNotEmpty()) {
            return createTrackListFromJson(getHistoryString())
        } else {
            return arrayListOf<Track>()
        }
    }

    override fun addTrackToHistory(track: Track) {
        val historyTrackList = getHistoryList()
        historyTrackList.remove(track)
        if (historyTrackList.size >= MAX_NUMBER_OF_TRACK_HISTORY) {
            historyTrackList.removeAt(historyTrackList.size - 1)
        }
        historyTrackList.add(0, track)
        updateHistory(historyTrackList)
    }

    override fun updateHistory(updatedHistoryList: ArrayList<Track>) {
        val updatedHistoryJson = createJsonFromTrackList(updatedHistoryList)
        historyRepository.updateTrackHistory(updatedHistoryJson)
    }


    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    fun createTrackListFromJson(json: String): ArrayList<Track> {
        var historyList = ArrayList<Track>()
        if (!json.isNullOrEmpty()) {
            val sType = object : TypeToken<ArrayList<Track>>() {}.type
            historyList = Gson().fromJson(json, sType)
        }
        return historyList
    }


    fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }
}