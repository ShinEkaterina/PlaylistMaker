package com.example.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.util.App
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.data.search.network.ErrorCode

class SearchActivity : AppCompatActivity() {

    private var searchText: String = ""

    private lateinit var trackAdapter: TrackAdapter
    private val tracksInteractor = Creator.provideTracksInteractor(this)


    private val trackList: ArrayList<Track> = arrayListOf()
    private var historyList = ArrayList<Track>()

    private lateinit var binding: ActivitySearchBinding


    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY_ML = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_ML = 2000L
    }


    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private val searchRunnable = Runnable { findTrack() }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_ML)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_ML)
        }
        return current
    }

    private fun findTrack() {
        if (searchText.isNotEmpty()) {

            binding.progressBar.visibility = View.VISIBLE
            binding.trackNotFoundVidget.setVisibility(View.GONE)
            binding.noInternetVidget.setVisibility(View.GONE)
            binding.recycleViewTracks.setVisibility(View.GONE)
            hideHistory()

            tracksInteractor.searchTracks(searchText, object : TracksInteractor.TrackConsumer {
                override fun consume(
                    foundTracks: List<Track>?,
                    errorCode: ErrorCode?,
                    errorMessage: String?
                ) {
                    handler.post {
                        binding.progressBar.visibility = View.GONE
                        if (foundTracks != null) {
                            trackList.clear()
                            trackList.addAll(foundTracks)
                            trackAdapter.tracks = trackList
                            trackAdapter.notifyDataSetChanged()
                            binding.progressBar.visibility = View.GONE
                            binding.recycleViewTracks.setVisibility(View.VISIBLE)
                        }
                        if (errorCode != null) {
                            when (errorCode) {
                                ErrorCode.NO_INTERNET -> {
                                    binding.noInternetVidget.setVisibility(View.VISIBLE)
                                    Log.d("SEARCH_LOG", "Error message: ${errorMessage}")
                                }

                                ErrorCode.UNKNOWN_ERROR -> {
                                    binding.noInternetVidget.setVisibility(View.VISIBLE)
                                    Log.d("SEARCH_LOG", "Error message: ${errorMessage}")
                                }

                                ErrorCode.NOTHING_FOUND -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.trackNotFoundVidget.setVisibility(View.VISIBLE)
                                }

                            }
                        }

                    }
                }
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchToolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT).toString()
        }
        binding.searchEditText.setText(searchText)

        binding.clearButton.setOnClickListener {
            //Clear text
            binding.searchEditText.setText("")

            //Hide the keyboard
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            binding.searchEditText.clearFocus()

            //clear trackList
            trackList.clear()
            trackAdapter.tracks = trackList
            trackAdapter.notifyDataSetChanged()

            //clear screen
            binding.trackNotFoundVidget.setVisibility(View.GONE)
            binding.noInternetVidget.setVisibility(View.GONE)

            // show history
            showHistory()
        }

        binding.searchEditText.addTextChangedListener(searchTextWatcher)

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTrack()
                true
            } else (false)
        }

        binding.updateButton.setOnClickListener { findTrack() }

        trackAdapter = TrackAdapter {
            if (clickDebounce()) {
                SearchHistory.addTrack(it)

                val intent = Intent(this, AudioPlayerActivity::class.java)
                intent.putExtra(App.TRACK, it)
                startActivity(intent)
            }
        }
        trackAdapter.tracks = trackList

        binding.recycleViewTracks.layoutManager = LinearLayoutManager(this)
        binding.recycleViewTracks.adapter = trackAdapter

        historyList.clear()
        historyList = SearchHistory.fillInList()

        binding.clearHistoryButton.setOnClickListener {
            SearchHistory.clear()
            historyList.clear()
            trackAdapter.tracks = historyList
            trackAdapter.notifyDataSetChanged()
            hideHistory()
        }

        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            focusVisibility(hasFocus)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun focusVisibility(hasFocus: Boolean) {
        historyList = SearchHistory.fillInList()
        trackAdapter.tracks = historyList
        trackAdapter.notifyDataSetChanged()
        if (hasFocus && binding.searchEditText.text.isEmpty() && historyList.isNotEmpty()) {
            binding.searchHistory.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.VISIBLE
        } else {
            hideHistory()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory() {
        historyList = SearchHistory.fillInList()
        trackAdapter.tracks = historyList
        trackAdapter.notifyDataSetChanged()
        if (historyList.isNotEmpty()) {
            binding.searchHistory.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.VISIBLE
        } else {
            hideHistory()
        }
    }

    private fun hideHistory() {
        binding.searchHistory.visibility = View.GONE
        binding.clearHistoryButton.visibility = View.GONE
        trackAdapter.tracks = trackList

    }

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                searchText = s.toString()
                searchDebounce()
            }
            binding.clearButton.visibility = clearButtonVisibility(s)
        }

        override fun afterTextChanged(s: Editable?) {
            // empty
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT).toString()
        }
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }
}