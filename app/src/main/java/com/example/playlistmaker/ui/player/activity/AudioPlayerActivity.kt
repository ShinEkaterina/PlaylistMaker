package com.example.playlistmaker.ui.player.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.util.App.Companion.TRACK
import com.example.playlistmaker.R
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.Formater
import com.example.playlistmaker.presentation.Mapper
import com.example.playlistmaker.presentation.player.model.TrackInfo
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel


class AudioPlayerActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Go back arrow button
        binding.toolbar.setNavigationOnClickListener { finish() }

        //define track
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java)
        } else {
            intent.getParcelableExtra(TRACK)
        } as Track

        val trackInfo = Mapper.mapTrackToTrackInfo(track)
        val url = track.previewUrl // url превью 30 сек.

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory()
        )[AudioPlayerViewModel::class.java]

        viewModel.getStatePlayerLiveData().observe(this) { state ->
            changeState(state)
        }

        viewModel.getCurrentTimerLiveData().observe(this) { currentTimer ->
            changeTimer(currentTimer)
        }
        viewModel.preparePlayer(url)

        binding.playButton.setOnClickListener{
            viewModel.changePlayerState()
        }
        viewModel.preparePlayer(url)

        initPlayerScreen(trackInfo)
    }

    private fun changeTimer(currentTimer: Int) {
        binding.durationTrackPlay.text = Formater.formayMillsTimeToDuration(currentTimer)
    }

    private fun changeState(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PAUSED -> {
                binding.playButton.setImageResource(R.drawable.play_button)
            }

            PlayerState.STATE_PLAYING -> {
                binding.playButton.setImageResource(R.drawable.pause_button)
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_DEFAULT -> {
                binding.playButton.setImageResource(R.drawable.play_button)
                binding.durationTrackPlay.text = "00:00"
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()

    }

    override fun onResume() {
        viewModel.onResume()
        super.onResume()

    }

    private fun initPlayerScreen(trackInfo: TrackInfo) {
        binding.trackName.text = trackInfo.name
        binding.artistName.text = trackInfo.artistName
        binding.trackDuration.text = trackInfo.duration
        binding.durationTrackPlay.setText(R.string.time_00)

        if (trackInfo.collectionName.isNullOrEmpty()) {
            binding.trackAlbum.visibility = View.GONE
            binding.album.visibility = View.GONE
        } else {
            binding.trackAlbum.text = trackInfo.collectionName
        }

        binding.trackYear.text = trackInfo.releaseYear
        binding.trackGenre.text = trackInfo.genreName
        binding.trackCountry.text = trackInfo.country
        Glide.with(binding.trackImage).load(trackInfo.artworkUrl)
            .placeholder(R.drawable.placeholder).centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.album_radius)))
            .into(binding.trackImage)
    }

}