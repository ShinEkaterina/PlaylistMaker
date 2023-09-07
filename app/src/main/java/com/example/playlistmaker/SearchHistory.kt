package com.example.playlistmaker

import com.example.playlistmaker.util.App.Companion.KEY_FOR_HISTORY_LIST
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.App
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SIZE = 10

object SearchHistory {

    private fun saveTrackHistory(trackList: ArrayList<Track>) {
        val json = Gson().toJson(trackList)
        App.sharedPreferences.edit()
            .putString(KEY_FOR_HISTORY_LIST, json)
            .apply()
    }

    fun fillInList(): ArrayList<Track> {
        var historyList = ArrayList<Track>()
        val getShare = App.sharedPreferences.getString(KEY_FOR_HISTORY_LIST, null)
        if (!getShare.isNullOrEmpty()) {
            val sType = object : TypeToken<ArrayList<Track>>() {}.type
            historyList = Gson().fromJson(getShare, sType)
        }
        return historyList
    }

    fun addTrack(track: Track) {
        val historyTrackList = fillInList()
        historyTrackList.remove(track)
        if (historyTrackList.size >= SIZE) {
            historyTrackList.removeAt(historyTrackList.size - 1)
        }
        historyTrackList.add(0, track)
        saveTrackHistory(historyTrackList)
    }

    fun clear() {
        App.sharedPreferences.edit()
            .remove(KEY_FOR_HISTORY_LIST)
            .apply()
    }
}