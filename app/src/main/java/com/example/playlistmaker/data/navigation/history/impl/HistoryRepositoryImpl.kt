package com.example.playlistmaker.data.navigation.history.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.navigation.history.HistoryRepository


const val KEY_FOR_HISTORY_LIST = "KEY_FOR_HISTORY_LIST"


class HistoryRepositoryImpl(
    private val sharedPref: SharedPreferences,
) : HistoryRepository {

    override fun updateTrackHistory(updatedHistory: String) {
        sharedPref.edit().putString(KEY_FOR_HISTORY_LIST, updatedHistory)
            .apply()
    }


    override fun getHistoryString(): String {
        return sharedPref.getString(KEY_FOR_HISTORY_LIST, null) ?: ""
    }

    override fun clearHistory() {
        sharedPref.edit().remove(KEY_FOR_HISTORY_LIST).apply()
    }


}