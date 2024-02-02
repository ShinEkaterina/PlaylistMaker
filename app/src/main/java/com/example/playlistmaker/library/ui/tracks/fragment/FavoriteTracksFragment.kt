package com.example.playlistmaker.library.ui.tracks.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.library.ui.tracks.view_model.FavTracksFragmentState
import com.example.playlistmaker.library.ui.tracks.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment : Fragment(), TrackAdapter.ClickListener {

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private var onTrackClickDebounce: (Track) -> Unit = {}

    override fun onTrackClick(track: Track) {
        onTrackClickDebounce(track)
    }

    override fun onTrackLongClick(track: Track) {
    }


    private var adapter: TrackAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY_MILLISECONDS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            // viewModel.saveTrackToHistory(TrackToTrackDtoMapper().invoke(track))
            findNavController().navigate(
                R.id.action_libraryFragment_to_playerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        adapter = TrackAdapter(ArrayList(), this@FavoriteTracksFragment)
        binding.rvTracks.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onTrackClickDebounce = {}

    }

    private fun render(state: FavTracksFragmentState) {
        when (state) {
            is FavTracksFragmentState.Favorites -> showContent(state.favorites)
            is FavTracksFragmentState.NoFavoriteTracks -> showEmpty()
            is FavTracksFragmentState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        with(binding) {
            rvTracks.isVisible = false
            clErrorWidget.isVisible = false
            loadingIndicator.isVisible = true
        }
    }

    private fun showEmpty() {
        with(binding) {
            rvTracks.isVisible = false
            loadingIndicator.isVisible = false
            clErrorWidget.isVisible = true
        }
    }

    private fun showContent(favorites: List<Track>) {
        with(binding) {
            loadingIndicator.isVisible = false
            clErrorWidget.isVisible = false
            rvTracks.isVisible = true
        }
        adapter?.tracks?.clear()
        adapter?.tracks?.addAll(favorites)
        adapter?.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY_MILLISECONDS = 100L

    }
}