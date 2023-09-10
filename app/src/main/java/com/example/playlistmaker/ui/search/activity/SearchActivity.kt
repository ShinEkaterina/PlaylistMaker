package com.example.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.player.model.ErrorType
import com.example.playlistmaker.presentation.player.model.SearchScreenState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel

class SearchActivity : AppCompatActivity(), TrackAdapter.Listener {

    private var searchText: String = ""

    private lateinit var binding: ActivitySearchBinding

    private lateinit var searchTrackViewModel: SearchViewModel

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY_ML = 1000L
    }


    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_ML)
        }
        return current
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchToolbar.setNavigationOnClickListener { finish() }

        // create viewModel
        searchTrackViewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        searchTrackViewModel.getSearchTrackStatusLiveData().observe(this) { updatedStatus ->
            updatedViewBasedOnStatus(updatedStatus)
        }

        binding.searchEditText.setText(searchText)

        binding.clearButton.setOnClickListener {
            //Clear text
            binding.searchEditText.setText("")

            //Hide the keyboard
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            binding.searchEditText.clearFocus()

            // show history
            searchTrackViewModel.showHistory()
        }

        binding.searchEditText.addTextChangedListener(searchTextWatcher)

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrackViewModel.searchAction(searchText)
                true
            } else (false)
        }

        binding.updateButton.setOnClickListener { searchTrackViewModel.searchAction(searchText) }

        init()

        binding.clearHistoryButton.setOnClickListener {
            searchTrackViewModel.clearHistory()
            searchTrackViewModel.showHistory()
        }

        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            focusVisibility(hasFocus)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun focusVisibility(hasFocus: Boolean) {
        searchTrackViewModel.showHistory()
        if (hasFocus && binding.searchEditText.text.isEmpty()) {
            searchTrackViewModel.showHistory()
        } else {
            showEmpty()
        }

    }

    // добавление трека в историю по клику и открыте в аудиоплеере
    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchTrackViewModel.addNewTrackToHistory(track)
            searchTrackViewModel.getHistory()
            searchTrackViewModel.openTrackAudioPlayer(track)
        }
    }


    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                searchText = s.toString()
                searchTrackViewModel.searchDebounce(searchText)
            }
            binding.clearButton.visibility = clearButtonVisibility(s)
        }

        override fun afterTextChanged(s: Editable?) {
            // empty
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

    fun updatedViewBasedOnStatus(updatedStatus: SearchScreenState) {
        when {
            updatedStatus.isLoading -> showLoading()
            updatedStatus.toShowHistory ->
                when {
                    updatedStatus.history.isNotEmpty() -> showHistoryUI(updatedStatus.history)
                    else -> showHistoryIsEmpty(updatedStatus.history)
                }

            (updatedStatus.errorType != null) ->
                when (updatedStatus.errorType) {
                    ErrorType.NOTHINF_FOUND -> showEmpty()
                    ErrorType.NO_INTERNET -> showError()
                }

            else -> showContent(updatedStatus.tracks)
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        searchTrackViewModel.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        searchTrackViewModel.onResume()
    }


    fun showLoading() {
        binding.apply {
            recycleViewTracks.visibility = View.GONE
            binding.trackNotFoundVidget.setVisibility(View.GONE)
            binding.noInternetVidget.setVisibility(View.GONE)
            progressBar.visibility = View.VISIBLE
            clearHistoryButton.visibility = View.GONE
            searchHistory.visibility = View.GONE
        }
    }

    fun showError() {
        binding.apply {
            recycleViewTracks.visibility = View.GONE
            progressBar.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            searchHistory.visibility = View.GONE
            binding.trackNotFoundVidget.setVisibility(View.GONE)
            binding.noInternetVidget.setVisibility(View.VISIBLE)
        }
    }

    fun showEmpty() {
        binding.apply {
            recycleViewTracks.visibility = View.GONE
            progressBar.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            searchHistory.visibility = View.GONE
            binding.trackNotFoundVidget.setVisibility(View.VISIBLE)
            binding.noInternetVidget.setVisibility(View.GONE)
        }
    }

    fun showContent(tracks: List<Track>) {
        binding.apply {
            recycleViewTracks.adapter = TrackAdapter(ArrayList(tracks), this@SearchActivity)
            recycleViewTracks.visibility = View.VISIBLE
            binding.trackNotFoundVidget.setVisibility(View.GONE)
            binding.noInternetVidget.setVisibility(View.GONE)
            progressBar.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            searchHistory.visibility = View.GONE
        }
    }

    fun showHistoryUI(updatedHistory: List<Track>) {
        binding.apply {
            recycleViewTracks.adapter = TrackAdapter(ArrayList(updatedHistory), this@SearchActivity)
            recycleViewTracks.visibility = View.VISIBLE
            binding.trackNotFoundVidget.setVisibility(View.GONE)
            binding.noInternetVidget.setVisibility(View.GONE)
            progressBar.visibility = View.GONE
            clearHistoryButton.visibility = View.VISIBLE
            searchHistory.visibility = View.VISIBLE

        }
    }

    fun showHistoryIsEmpty(updatedHistory: List<Track>) {
        binding.apply {
            recycleViewTracks.adapter = TrackAdapter(ArrayList(updatedHistory), this@SearchActivity)
            recycleViewTracks.visibility = View.VISIBLE
            binding.trackNotFoundVidget.setVisibility(View.GONE)
            binding.noInternetVidget.setVisibility(View.GONE)
            progressBar.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            searchHistory.visibility = View.GONE

        }
    }

    private fun init() {
        binding.apply {
            recycleViewTracks.layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }
}