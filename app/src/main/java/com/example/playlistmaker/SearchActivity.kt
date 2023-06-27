package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private var searchText: String = ""

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private lateinit var recycleViewTracks: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView


    private lateinit var trackNotFoundVidget: View
    private lateinit var noInternetVidget: View
    private lateinit var historyWidget: View

    private val trackList: ArrayList<Track> = arrayListOf()
    private var historyList = ArrayList<Track>()

    private lateinit var clearHistoryButton: Button

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val BASE_URL = "https://itunes.apple.com"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val iTunesService = retrofit.create(iTunesApi::class.java)

    private fun findTrack(text: String) {
        trackNotFoundVidget.setVisibility(View.GONE)
        noInternetVidget.setVisibility(View.GONE)
        recycleViewTracks.setVisibility(View.GONE)


        iTunesService
            .search(text)
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
                            trackNotFoundVidget.setVisibility(View.VISIBLE)
                        } else {
                            trackList.clear()
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            recycleViewTracks.setVisibility(View.VISIBLE)

                        }

                    } else {
                        noInternetVidget.setVisibility(View.VISIBLE)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    Log.d("SEARCH_LOG", "FAIL")
                    noInternetVidget.setVisibility(View.VISIBLE)
                }
            })

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        trackNotFoundVidget = findViewById<View>(R.id.not_found_widget)
        noInternetVidget = findViewById<View>(R.id.network_problem_widget)
        historyWidget = findViewById<View>(R.id.history_widget)


        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        searchEditText = findViewById<EditText>(R.id.input_edit_text)
        clearButton = findViewById<ImageView>(R.id.clear_icon)

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT).toString()
        }
        searchEditText.setText(searchText)

        clearButton.setOnClickListener {
            //Clear text
            searchEditText.setText("")

            //Hide the keyboard
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            searchEditText.clearFocus()

            //clear trackList
            trackList.clear()
            trackAdapter.notifyDataSetChanged()

            // show history
            showHistory()
        }

        searchEditText.addTextChangedListener(searchTextWatcher)

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findTrack(searchText)
                true
            }
            false
        }

        val updateButton = findViewById<Button>(R.id.update_button)
        updateButton.setOnClickListener { findTrack(searchText) }

        trackAdapter = TrackAdapter(trackList)
        historyAdapter = TrackAdapter(historyList)

        recycleViewTracks = findViewById(R.id.search_list)
        recycleViewTracks.layoutManager = LinearLayoutManager(this)
        recycleViewTracks.adapter = trackAdapter

        historyRecyclerView = findViewById(R.id.history_recycle_view)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

        historyList.clear()
        historyList = SearchHistory.fillInList()

        clearHistoryButton = findViewById(R.id.clear_history_button)
        clearHistoryButton.setOnClickListener {
            SearchHistory.clear()
            historyList.clear()
            historyAdapter.notifyDataSetChanged()
            historyWidget.visibility = View.GONE
        }

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            focusVisibility(hasFocus)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun focusVisibility(hasFocus: Boolean) {
        historyList.clear()
        historyList = SearchHistory.fillInList()
        historyAdapter.notifyDataSetChanged()
        if (hasFocus && searchEditText.text.isEmpty() && historyList.isNotEmpty()) {
            historyWidget.visibility = View.VISIBLE
            historyAdapter.notifyDataSetChanged()
        } else {
            historyWidget.visibility = View.GONE
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory() {
        historyList.clear()
        historyList = SearchHistory.fillInList()
        historyAdapter.notifyDataSetChanged()
        if (historyList.isNotEmpty()) {
            historyWidget.visibility = View.VISIBLE
        } else {
            historyWidget.visibility = View.GONE
        }
    }

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                searchText = s.toString()
            }
            clearButton.visibility = clearButtonVisibility(s)
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