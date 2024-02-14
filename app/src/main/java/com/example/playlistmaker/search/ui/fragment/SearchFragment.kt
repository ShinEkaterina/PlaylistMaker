package com.example.playlistmaker.search.ui.fragment

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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.player.presentation.model.ErrorType
import com.example.playlistmaker.player.presentation.model.SearchScreenState
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackAdapter.ClickListener {

    private var searchText: String = ""

    private val searchTrackViewModel by viewModel<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var onTrackClickDebounce: (Track) -> Unit


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchTrackViewModel.getSearchTrackStatusLiveData()
            .observe(viewLifecycleOwner) { updatedStatus ->
                updatedViewBasedOnStatus(updatedStatus)
            }


        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLISECONDS,
            viewLifecycleOwner.lifecycleScope,
            true
        ) { track ->
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }

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

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
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
    override fun onTrackClick(track: Track) {
        searchTrackViewModel.addNewTrackToHistory(track)
        searchTrackViewModel.getHistory()
        onTrackClickDebounce(track)
    }

    override fun onTrackLongClick(track: Track) {
        //do nothing
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
            recycleViewTracks.isVisible = false
            binding.trackNotFoundVidget.isVisible = false
            binding.noInternetVidget.isVisible = false
            progressBar.isVisible = true
            clearHistoryButton.isVisible = false
            searchHistory.isVisible = false
        }
    }

    fun showError() {
        binding.apply {
            recycleViewTracks.isVisible = false
            progressBar.isVisible = false
            clearHistoryButton.isVisible = false
            searchHistory.isVisible = false
            binding.trackNotFoundVidget.isVisible = false
            binding.noInternetVidget.isVisible = true
        }
    }

    fun showEmpty() {
        binding.apply {
            recycleViewTracks.isVisible = false
            progressBar.isVisible = false
            clearHistoryButton.isVisible = false
            searchHistory.isVisible = false
            binding.trackNotFoundVidget.isVisible = true
            binding.noInternetVidget.isVisible = false
        }
    }

    fun showContent(tracks: List<Track>) {
        binding.apply {
            recycleViewTracks.adapter = TrackAdapter(ArrayList(tracks), this@SearchFragment)
            recycleViewTracks.isVisible = true
            binding.trackNotFoundVidget.isVisible = false
            binding.noInternetVidget.isVisible = false
            progressBar.isVisible = false
            clearHistoryButton.isVisible = false
            searchHistory.isVisible = false
        }
    }

    fun showHistoryUI(updatedHistory: List<Track>) {
        binding.apply {
            recycleViewTracks.adapter = TrackAdapter(ArrayList(updatedHistory), this@SearchFragment)
            recycleViewTracks.isVisible = true
            binding.trackNotFoundVidget.isVisible = false
            binding.noInternetVidget.isVisible = false
            progressBar.isVisible = false
            clearHistoryButton.isVisible = true
            searchHistory.isVisible = true

        }
    }

    fun showHistoryIsEmpty(updatedHistory: List<Track>) {
        binding.apply {
            recycleViewTracks.adapter = TrackAdapter(ArrayList(updatedHistory), this@SearchFragment)
            recycleViewTracks.isVisible = true
            binding.trackNotFoundVidget.isVisible = false
            binding.noInternetVidget.isVisible = false
            progressBar.isVisible = false
            clearHistoryButton.isVisible = false
            searchHistory.isVisible = false

        }
    }

    private fun init() {
        binding.apply {
            recycleViewTracks.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLISECONDS = 100L
    }
}