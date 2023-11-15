package com.example.playlistmaker.ui.search.fragment

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.player.model.ErrorType
import com.example.playlistmaker.presentation.player.model.SearchScreenState
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.TrackAdapter
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackAdapter.Listener {

    private var searchText: String = ""

    private val searchTrackViewModel by viewModel<SearchViewModel>()

    private var isClickAllowed = true

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var onTrackClickDebounce: (Unit) -> Unit


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            onTrackClickDebounce(Unit)
        }
        return current
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchTrackViewModel.getSearchTrackStatusLiveData()
            .observe(viewLifecycleOwner) { updatedStatus ->
                updatedViewBasedOnStatus(updatedStatus)
            }
        onTrackClickDebounce = debounce<Unit>(
            delayMillis = CLICK_DEBOUNCE_DELAY_MILLISECONDS,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true,
            action = {
                isClickAllowed = true
            }
        )

        binding.searchEditText.setText(searchText)

        binding.clearButton.setOnClickListener {
            //Clear text
            binding.searchEditText.setText("")

            //Hide the keyboard
            val keyboard =
                requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
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
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerActivity,
                AudioPlayerActivity.createArgs(track)
            )

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        searchTrackViewModel.onResume()
    }


    fun showLoading() {
        binding.apply {
            recycleViewTracks.visibility = View.GONE
            binding.trackNotFoundVidget.visibility = View.GONE
            binding.noInternetVidget.visibility = View.GONE
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
            binding.trackNotFoundVidget.visibility = View.GONE
            binding.noInternetVidget.visibility = View.VISIBLE
        }
    }

    fun showEmpty() {
        binding.apply {
            recycleViewTracks.visibility = View.GONE
            progressBar.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            searchHistory.visibility = View.GONE
            binding.trackNotFoundVidget.visibility = View.VISIBLE
            binding.noInternetVidget.visibility = View.GONE
        }
    }

    fun showContent(tracks: List<Track>) {
        binding.apply {
            recycleViewTracks.adapter = TrackAdapter(ArrayList(tracks), this@SearchFragment)
            recycleViewTracks.visibility = View.VISIBLE
            binding.trackNotFoundVidget.visibility = View.GONE
            binding.noInternetVidget.visibility = View.GONE
            progressBar.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            searchHistory.visibility = View.GONE
        }
    }

    fun showHistoryUI(updatedHistory: List<Track>) {
        binding.apply {
            recycleViewTracks.adapter = TrackAdapter(ArrayList(updatedHistory), this@SearchFragment)
            recycleViewTracks.visibility = View.VISIBLE
            binding.trackNotFoundVidget.visibility = View.GONE
            binding.noInternetVidget.visibility = View.GONE
            progressBar.visibility = View.GONE
            clearHistoryButton.visibility = View.VISIBLE
            searchHistory.visibility = View.VISIBLE

        }
    }

    fun showHistoryIsEmpty(updatedHistory: List<Track>) {
        binding.apply {
            recycleViewTracks.adapter = TrackAdapter(ArrayList(updatedHistory), this@SearchFragment)
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
            recycleViewTracks.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY_MILLISECONDS = 1000L
    }
}