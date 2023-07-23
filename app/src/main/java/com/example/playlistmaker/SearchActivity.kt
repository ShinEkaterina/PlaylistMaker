package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var searchText: String = ""

    private lateinit var trackAdapter: TrackAdapter


    private val trackList: ArrayList<Track> = arrayListOf()
    private var historyList = ArrayList<Track>()

    private lateinit var binding: ActivitySearchBinding


    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val BASE_URL = "https://itunes.apple.com"
        private const val CLICK_DEBOUNCE_DELAY_ML = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_ML = 2000L
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val iTunesService = retrofit.create(iTunesApi::class.java)

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
        if(searchText.isNotEmpty()){


        binding.progressBar.visibility = View.VISIBLE
        binding.trackNotFoundVidget.setVisibility(View.GONE)
        binding.noInternetVidget.setVisibility(View.GONE)
        binding.recycleViewTracks.setVisibility(View.GONE)
        hideHistory()


        iTunesService
            .search(binding.searchEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    Log.d("SEARCH_LOG", "Count track: ${response.body()?.resultCount}")
                    Log.d("SEARCH_LOG", "Code: ${response.code()}")
                    Log.d("SEARCH_LOG", "TrackList: ${response.body()?.results.toString()}")
                    if (response.code() == 200) {

                        if (response.body()?.resultCount == 0) {
                            binding.progressBar.visibility = View.GONE
                            binding.trackNotFoundVidget.setVisibility(View.VISIBLE)
                        } else {
                            trackList.clear()
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.tracks = trackList
                            trackAdapter.notifyDataSetChanged()
                            binding.progressBar.visibility = View.GONE
                            binding.recycleViewTracks.setVisibility(View.VISIBLE)

                        }

                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.noInternetVidget.setVisibility(View.VISIBLE)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    Log.d("SEARCH_LOG", "FAIL")
                    binding.noInternetVidget.setVisibility(View.VISIBLE)
                }
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

            // show history
            showHistory()
        }

        binding.searchEditText.addTextChangedListener(searchTextWatcher)

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTrack()
                true
            }
            false
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
        //   historyList.clear()
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
        //   historyList.clear()
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