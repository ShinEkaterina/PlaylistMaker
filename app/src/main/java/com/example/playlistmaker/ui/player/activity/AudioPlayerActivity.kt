package com.example.playlistmaker.ui.player.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.Formater
import com.example.playlistmaker.presentation.Mapper
import com.example.playlistmaker.presentation.player.model.TrackInfo
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.util.App.Companion.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayerActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>()


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

        viewModel.getStatePlayerLiveData().observe(this) { state ->
            changeState(state)
        }

        viewModel.getCurrentTimerLiveData().observe(this) { currentTimer ->
            changeTimer(currentTimer)
        }
        viewModel.preparePlayer(url)

        binding.ivPlayButton.setOnClickListener{
            viewModel.changePlayerState()
        }
        viewModel.preparePlayer(url)

        initPlayerScreen(trackInfo)
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
                binding.tvDurationPlay.text = "00:00"
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
    }

}