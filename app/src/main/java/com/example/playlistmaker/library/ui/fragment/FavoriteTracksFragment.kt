package com.example.playlistmaker.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.library.ui.view_model.FavTracksFragmentState
import com.example.playlistmaker.library.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteTracksFragment : Fragment(), TrackAdapter.Listener {

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private var onTrackClickDebounce: (Track) -> Unit = {}


    override fun onClick(track: Track) {
        onTrackClickDebounce(track)
    }


    private lateinit var errorWidget: ConstraintLayout
    private lateinit var favoritesList: RecyclerView

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

        errorWidget = binding.errorWidget
        favoritesList = binding.favoritesRecyclerView

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
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
        adapter = TrackAdapter(ArrayList(),this@FavoriteTracksFragment)
        favoritesList.adapter = adapter

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
            is FavTracksFragmentState.FAVORITES -> showContent(state.favorites)
            is FavTracksFragmentState.NO_FAVORITE_TRACKS -> showEmpty()
            is FavTracksFragmentState.LOADING -> showLoading()
        }
    }

    private fun showLoading() {
        favoritesList.visibility = View.GONE
        errorWidget.visibility = View.GONE
        binding.loadingIndicator.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        favoritesList.visibility = View.GONE
        binding.loadingIndicator.visibility = View.GONE
        errorWidget.visibility = View.VISIBLE
    }

    private fun showContent(favorites: List<Track>) {
        binding.loadingIndicator.visibility = View.GONE
        errorWidget.visibility = View.GONE
        favoritesList.visibility = View.VISIBLE
       // adapter = TrackAdapter(ArrayList(favorites), this@FavoriteTracksFragment)


        adapter?.tracks?.clear()
        adapter?.tracks?.addAll(favorites)
        adapter?.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 10L

    }
}