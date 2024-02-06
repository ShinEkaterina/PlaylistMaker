package com.example.playlistmaker.library.ui.playlist.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.common.presentation.ConfirmationDialog
import com.example.playlistmaker.common.presentation.rightEndingMinutes
import com.example.playlistmaker.common.presentation.rightEndingTrack
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.library.ui.playlist.view_model.PlaylistTracksState
import com.example.playlistmaker.library.ui.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.util.CLICK_DEBOUNCE_DELAY_MILLISECONDS
import com.example.playlistmaker.util.PLAYLIST_ID
import com.example.playlistmaker.util.createPlaylistFromJson
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors


class PlaylistFragment : Fragment(), TrackAdapter.ClickListener {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding
        get() = _binding!!

    private lateinit var playlist: Playlist
    private var adapter: TrackAdapter? = null
    private lateinit var bottomSheetTracksBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetSettingsBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var onTrackClickDebounce: (Track) -> Unit


    private val viewModel by viewModel<PlaylistViewModel>()

    private val confirmator: ConfirmationDialog by inject { parametersOf(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLISECONDS,
            viewLifecycleOwner.lifecycleScope,
            true
        ) { track ->
            findNavController().navigate(
                R.id.action_playlistFragment_to_playerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }

        playlist = createPlaylistFromJson(requireArguments().getString(PLAYLIST_ID))

        binding.rvTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = TrackAdapter(ArrayList(), this@PlaylistFragment)
        binding.rvTracks.adapter = adapter

        viewModel.apply {
            playlistTracks.observe(viewLifecycleOwner, ::renderState)
            playlist.observe(viewLifecycleOwner, ::updatePlaylist)
        }


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivShare.setOnClickListener {
            sharePlaylist()
        }

        binding.ivSettings.setOnClickListener {
            showBottomSheetSettings()
        }

        binding.tvShare.setOnClickListener {
            sharePlaylist()
            bottomSheetSettingsBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.tvDeletePlaylist.setOnClickListener {
            showDialogDeletePlaylist()
        }

        binding.tvEditInformation.setOnClickListener {
/*            findNavController().navigate(
                R.id.action_playlistDescriptionFragment_to_playlistEditFragment,
                bundleOf(PLAYLIST_ID to createJsonFromPlaylist(playlist))
            )*/
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        bottomSheetTracksBehavior = BottomSheetBehavior.from(binding.bottomSheetTracks)
        bottomSheetSettingsBehavior = BottomSheetBehavior.from(binding.bottomSheetSettings)


        bottomSheetSettingsBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
               // binding.overlay.alpha = slideOffset
            }
        })
    }

    private fun initView() {
        binding.tvPlaylistName.text = playlist.name
        binding.tvPlaylistOverview.text = playlist.description
        updateCountSongs()
        updateSumTime()
        Glide.with(requireContext())
            .load(viewModel.getImage(playlist.imageName))
            .placeholder(R.drawable.playlist_card_placeholder)
            .into(binding.ivPlaceholder)

        bottomSheetTracksBehavior.peekHeight = binding.container.height * 3 / 10
        bottomSheetTracksBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetSettingsBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetSettingsBehavior.peekHeight = binding.container.height * 4 / 10


    }

    private fun updatePlaylist(playlistUpdate: Playlist) {
        playlist = playlistUpdate
        initView()
    }

    private fun updateSumTime() {
        val sumDuration = if (!adapter!!.tracks.isNullOrEmpty())
            adapter!!.tracks.sumOf { it.trackTimeMillis }
        else
            0
        binding.tvMinutes.text = rightEndingMinutes(
            TimeUnit.MILLISECONDS.toMinutes(sumDuration).toInt()
        )
    }

    private fun updateCountSongs() {
        binding.tvCount.text = rightEndingTrack(adapter!!.itemCount)
    }

    private fun renderState(state: PlaylistTracksState) {
        when (state) {
            is PlaylistTracksState.Content -> showData(state.data)
            is PlaylistTracksState.Empty -> showEmpty()
            else -> {}
        }
    }

    private fun showData(listTrack: List<Track>) {
        adapter!!.apply {
            tracks.clear()
            tracks.addAll(listTrack)
            notifyDataSetChanged()
        }
        updateSumTime()
        updateCountSongs()
    }

    private fun showEmpty() {
        adapter!!.apply {
            tracks.clear()
            notifyDataSetChanged()
        }
        updateSumTime()
        updateCountSongs()
    }

    private fun showDialogDeleteTrack(track: Track) {
        confirmator.showConfirmationDialog(
            title = getString(R.string.delete_track),
            message = getString(R.string.delete_track_message),
            positiveButton = getString(R.string.delete),
            negativeButton = getString(R.string.cancel),
            positiveAction = {
                viewModel.deleteTrackFromPlaylist(playlist.id, track)
            },
            negativeAction = {
            },
            positiveColor = ContextCompat.getColor(requireContext(), R.color.blue),
            negativeColor = ContextCompat.getColor(requireContext(), R.color.blue)
        )

    }

    private fun showDialogDeletePlaylist() {
        confirmator.showConfirmationDialog(
            title = getString(R.string.delete_playlist),
            message = getString(R.string.delete_playlist_message),
            positiveButton = getString(R.string.delete),
            negativeButton = getString(R.string.cancel),
            positiveAction = {
                viewModel.deletePlaylist(playlist.id)
                findNavController().popBackStack()            },
            negativeAction = {
            },
            positiveColor = ContextCompat.getColor(requireContext(), R.color.blue),
            negativeColor = ContextCompat.getColor(requireContext(), R.color.blue)
        )
    }

    private fun sharePlaylist() {
        adapter!!.apply {
            if (tracks.isNullOrEmpty())
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_tracks_for_sharing),
                    Toast.LENGTH_SHORT
                ).show()
            else
                shareTracks()
        }
    }

    private fun shareTracks() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getPlaylistInfo())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun getPlaylistInfo(): String {
        return "${playlist.name}\n" +
                "${playlist.description}\n" +
                "${playlist.tracks!!.size}\n" +
                getTracksInfo()
    }

    private fun getTracksInfo(): String {
        val tracksInfo = adapter!!.tracks.mapIndexed { index, track ->
            "${index + 1}.${track.artistName} - ${track.trackName}(${
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(track.trackTimeMillis)
            })"
        }
        return tracksInfo.stream().collect(Collectors.joining("\n"))
    }

    private fun showBottomSheetSettings() {
        bottomSheetSettingsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        Glide.with(requireContext())
            .load(viewModel.getImage(playlist.imageName))
            .placeholder(R.drawable.playlist_card_placeholder)
            .into(binding.ivPlaylistMini)
        binding.tvPlMiniTitle.text = playlist.name
        binding.tvPlMiniCount.text = rightEndingTrack(if(playlist.tracks.isNullOrEmpty()) 0 else playlist.tracks!!.size)


    }

    override fun onResume() {
        super.onResume()
        viewModel.getTracks(playlist.id)
        viewModel.updatePlaylist(playlist.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTrackClick(track: Track) {
        onTrackClickDebounce(track)
    }

    override fun onTrackLongClick(track: Track) {
        showDialogDeleteTrack(track)
        Log.d("My LOG", "LONG CLICK")
    }

}
