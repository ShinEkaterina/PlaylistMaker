package com.example.playlistmaker

import android.content.Context
import android.media.MediaPlayer
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
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    fun isDarkThemeEnabled(context: Context?): Boolean {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        return nightMode == AppCompatDelegate.MODE_NIGHT_YES
    }


    private var playerState = MediaplayerState.STATE_DEFAULT.code
    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable by lazy {
        Runnable {
            binding.durationTrackPlay.text = SimpleDateFormat(
                "mm:ss", Locale.getDefault()
            ).format(mediaPlayer.currentPosition.toLong())
            handler.postDelayed(runnable, 300)
        }
    }


    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = MediaplayerState.STATE_PREPARED.code
        }
        mediaPlayer.setOnCompletionListener {
            if(isDarkThemeEnabled(this))(
                    binding.playButton.setImageResource(R.drawable.play_button_night)
                    )
            else{
                binding.playButton.setImageResource(R.drawable.play_button)

            }
            playerState = MediaplayerState.STATE_PREPARED.code
            binding.durationTrackPlay.setText(R.string.time_00)
            handler.removeCallbacks(runnable)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        if(isDarkThemeEnabled(this))(
                binding.playButton.setImageResource(R.drawable.pause_button_night)
                )
        else{
            binding.playButton.setImageResource(R.drawable.pause_button)

        }

        playerState = MediaplayerState.STATE_PLAYING.code
        handler.post(runnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        if(isDarkThemeEnabled(this))(
                binding.playButton.setImageResource(R.drawable.play_button_night)
                )
        else{
            binding.playButton.setImageResource(R.drawable.play_button)

        }
        handler.removeCallbacks(runnable)
        playerState = MediaplayerState.STATE_PAUSED.code
    }

    private fun playbackControl() {
        when (playerState) {
            MediaplayerState.STATE_PLAYING.code -> {
                pausePlayer()
            }

            MediaplayerState.STATE_PREPARED.code, MediaplayerState.STATE_PAUSED.code -> {
                startPlayer()
            }
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

        goToPlayer(track)

        preparePlayer(track.previewUrl)

        binding.playButton.setOnClickListener {
            playbackControl()
        }


    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }

    private fun goToPlayer(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
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