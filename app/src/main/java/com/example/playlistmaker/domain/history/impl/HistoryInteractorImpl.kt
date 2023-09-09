package com.example.playlistmaker.domain.history.impl

import com.example.playlistmaker.data.navigation.history.HistoryRepository
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val MAX_NUMBER_OF_TRACK = 10



class HistoryInteractorImpl (private val historyRepository: HistoryRepository) : HistoryInteractor {

//TODO rewrite short
    override fun getHistoryString(): String {
        var historyString = historyRepository.getHistoryString()
        return if (historyString !=null) {
            historyString
        } else {
            ""
        }
    }

    override fun getHistoryList() : ArrayList<Track> {
        if (getHistoryString().isNotEmpty()) {
            return createTrackListFromJson(getHistoryString())
        }
        else {
            return arrayListOf<Track>()
        }
    }

    override fun addTrackToHistory(track: Track) {
        val historyTrackList = getHistoryList()
        historyTrackList.remove(track)
        if (historyTrackList.size >= MAX_NUMBER_OF_TRACK) {
            historyTrackList.removeAt(historyTrackList.size - 1)
        }
        historyTrackList.add(0, track)
        updateHistory(historyTrackList)
    }

    override fun updateHistory(updatedHistory: ArrayList<Track>) {
        val updatedHistoryJson = createJsonFromTrackList(updatedHistory)
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

    fun createTrackList1FromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }
}