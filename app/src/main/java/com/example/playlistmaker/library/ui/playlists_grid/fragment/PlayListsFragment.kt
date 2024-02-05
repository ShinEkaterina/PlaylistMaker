package com.example.playlistmaker.library.ui.playlists_grid.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.databinding.FragmentLibraryPlaylistsBinding
import com.example.playlistmaker.library.ui.playlists_grid.view_model.PlaylistsFragmentState
import com.example.playlistmaker.library.ui.playlists_grid.view_model.PlaylistsViewModel
import com.example.playlistmaker.util.PLAYLIST_ID
import com.example.playlistmaker.util.createJsonFromPlaylist
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class PlayListsFragment : Fragment(), PlaylistAdapter.PlaylistClickListener {

    private val viewModel: PlaylistsViewModel by activityViewModel()

    private var _binding: FragmentLibraryPlaylistsBinding? = null
    private val binding get() = _binding!!

    private var playlistAdapter: PlaylistAdapter? = null
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onPlaylistClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY_MILLISECONDS, lifecycleScope, false) { playlist ->
                findNavController().navigate(
                    R.id.action_libraryFragment_to_playlistFragment,
                    bundleOf(PLAYLIST_ID to createJsonFromPlaylist(playlist))
                )
            }


        playlistAdapter = PlaylistAdapter(R.layout.item_playlist_grid) { playlist ->
            onPlaylistClickDebounce(playlist)
        }

        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlaylists.adapter = playlistAdapter


        viewModel.libraryPlaylist.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.btnPlaylistCreate.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_playlistCreateFragment)
        }
    }

    private fun render(playlistState: PlaylistsFragmentState) {
        when (playlistState) {
            is PlaylistsFragmentState.Content -> showPlaylists(playlistState.data)
            is PlaylistsFragmentState.Empty -> showEmpty()
            is PlaylistsFragmentState.Loading -> showLoading()
        }
    }

    private fun showPlaylists(listPlaylist: List<Playlist>) {
        binding.llErrorWidget.visibility = View.GONE
        binding.rvPlaylists.visibility = View.VISIBLE

        playlistAdapter?.playlists?.clear()
        playlistAdapter?.playlists?.addAll(listPlaylist)
        playlistAdapter?.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.llErrorWidget.visibility = View.VISIBLE
        binding.rvPlaylists.visibility = View.GONE
    }

    private fun showLoading() {
        with(binding) {
            rvPlaylists.isVisible = false
            llErrorWidget.isVisible = false
            loadingIndicator.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    companion object {

        private const val CLICK_DEBOUNCE_DELAY_MILLISECONDS = 1000L

        @JvmStatic
        fun newInstance() = PlayListsFragment()
    }

    override fun onClick(playlist: Playlist) {
        onPlaylistClickDebounce(playlist)
        Log.d("LOGS", "Playlist click")    }
}