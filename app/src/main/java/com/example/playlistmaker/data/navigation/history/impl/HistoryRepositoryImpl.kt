package com.example.playlistmaker.data.navigation.history.impl

import android.content.Context
import com.example.playlistmaker.data.navigation.history.HistoryRepository

const val SEARCH_HISTORY = "search_history"
const val KEY_FOR_HISTORY_LIST = "KEY_FOR_HISTORY_LIST"


class HistoryRepositoryImpl(context: Context) : HistoryRepository {


    private var sharedPref = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE)


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