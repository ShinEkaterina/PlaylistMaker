package com.example.playlistmaker.player.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.search.presentation.Mapper
import com.example.playlistmaker.util.Formater
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AudioPlayerViewModel>()
    var track: Track? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Go back arrow button
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        //define track
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(TRACK, Track::class.java)
        } else {
            requireArguments().getParcelable(TRACK)
        } as Track


        val trackInfo = Mapper.mapTrackToTrackInfo(track)
        val url = track.previewUrl // url превью 30 сек.

        viewModel.getStatePlayerLiveData().observe(viewLifecycleOwner) { state ->
            changeState(state)
        }

        viewModel.getCurrentTimerLiveData().observe(viewLifecycleOwner) { currentTimer ->
            changeTimer(currentTimer)
        }
        viewModel.preparePlayer(url)

        initPlayerScreen(trackInfo)

        binding.ivPlayButton.setOnClickListener {
            viewModel.changePlayerState()
        }

        binding.ivLikeButton.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }
        viewModel.getIsFavorite().observe(viewLifecycleOwner ){isFavorite ->
            changeLikeButton(isFavorite)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


    override fun onResume() {
        viewModel.onResume()
        super.onResume()


    }

    private fun changeTimer(currentTimer: Int) {
        binding.tvDurationPlay.text = Formater.formayMillsTimeToDuration(currentTimer)
    }

    private fun changeState(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PAUSED -> {
                binding.ivPlayButton.setImageResource(R.drawable.play_button)
            }

            PlayerState.STATE_PLAYING -> {
                binding.ivPlayButton.setImageResource(R.drawable.pause_button)
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_DEFAULT -> {
                binding.ivPlayButton.setImageResource(R.drawable.play_button)
                binding.tvDurationPlay.text = getString(R.string.time_00)
            }
        }
    }

    private fun changeLikeButton(isFavorite: Boolean){
        if(isFavorite){
            binding.ivLikeButton.setImageResource(R.drawable.like_button_on)
        } else{
            binding.ivLikeButton.setImageResource(R.drawable.like_button_off)
        }
    }

    private fun initPlayerScreen(trackInfo: TrackInfo) {
        binding.trackName.text = trackInfo.name
        binding.artistName.text = trackInfo.artistName
        binding.tvDuration.text = trackInfo.duration
        binding.tvDurationPlay.setText(R.string.time_00)

        if (trackInfo.collectionName.isNullOrEmpty()) {
            binding.tvAlbum.visibility = View.GONE
            binding.album.visibility = View.GONE
        } else {
            binding.tvAlbum.text = trackInfo.collectionName
        }

        binding.tvYear.text = trackInfo.releaseYear
        binding.tvGenre.text = trackInfo.genreName
        binding.tvCountry.text = trackInfo.country
        Glide.with(binding.trackImage).load(trackInfo.artworkUrl)
            .placeholder(R.drawable.placeholder).centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.album_radius)))
            .into(binding.trackImage)
        viewModel.updateLikeButton(trackInfo.id)
    }

    companion object {
        const val TRACK = "track"

        fun createArgs(track: Track): Bundle = bundleOf(TRACK to track)
    }
}