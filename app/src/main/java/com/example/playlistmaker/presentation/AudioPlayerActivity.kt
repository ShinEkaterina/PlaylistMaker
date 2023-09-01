package com.example.playlistmaker.presentation

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.App.Companion.TRACK
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track


class AudioPlayerActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var playerInteractor: AudioPlayerInteractor

    fun isDarkThemeEnabled(): Boolean {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        return nightMode == AppCompatDelegate.MODE_NIGHT_YES
    }


    private val handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable by lazy {
        Runnable {
            binding.durationTrackPlay.text =
                Formater.formayMillsTimeToDuration(playerInteractor.getCurrentTime())
            handler.postDelayed(runnable, 300)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }


        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java)
        } else {
            intent.getParcelableExtra(TRACK)
        } as Track

        initPlayerScreen(track)
        preparePlayer(track)

        binding.playButton.setOnClickListener {
            playerInteractor.playbackControl(onStartPlayer = {
                if (isDarkThemeEnabled()) (binding.playButton.setImageResource(R.drawable.pause_button_night))
                else {
                    binding.playButton.setImageResource(R.drawable.pause_button)

                }
                playerInteractor.setPlayerState(PlayerState.STATE_PLAYING)
                handler.post(runnable)
            }, onPausePlayer = {
                if (isDarkThemeEnabled()) (binding.playButton.setImageResource(R.drawable.play_button_night))
                else {
                    binding.playButton.setImageResource(R.drawable.play_button)

                }
                handler.removeCallbacks(runnable)
                playerInteractor.setPlayerState(PlayerState.STATE_PAUSED)
            }

            )
        }
    }


    private fun preparePlayer(track: Track) {
        playerInteractor = Creator.provideAudioPlayerInteractor(track)
        playerInteractor.preparePlayer(prepare = {
            binding.playButton.isEnabled = true
        }, onComplete = {
            if (isDarkThemeEnabled()) (binding.playButton.setImageResource(R.drawable.play_button_night))
            else {
                binding.playButton.setImageResource(R.drawable.play_button)

            }
            binding.durationTrackPlay.setText(R.string.time_00)
            handler.removeCallbacks(runnable)
        })
    }


    override fun onPause() {
        super.onPause()
        playerInteractor.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.destroyPlayer()
        handler.removeCallbacks(runnable)
    }

    private fun initPlayerScreen(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackDuration.text = Formater.formayMillsTimeToDuration(track.trackTimeMillis)
        binding.durationTrackPlay.setText(R.string.time_00)

        if (track.collectionName.isNullOrEmpty()) {
            binding.trackAlbum.visibility = View.GONE
            binding.album.visibility = View.GONE
        } else {
            binding.trackAlbum.text = track.collectionName
        }

        binding.trackYear.text = track.getReleaseDateOnlyYear()
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country
        Glide.with(binding.trackImage).load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder).centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.album_radius)))
            .into(binding.trackImage)
    }

}