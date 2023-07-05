package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import java.text.SimpleDateFormat
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.App.Companion.TRACK
import java.util.Locale


class AudioPlayerActivity() : AppCompatActivity() {
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackDuration: TextView
    private lateinit var trackDurationPlay: TextView
    private lateinit var trackAlbum: TextView
    private lateinit var album: TextView
    private lateinit var trackYear: TextView
    private lateinit var trackGenre: TextView
    private lateinit var trackCountry: TextView
    private lateinit var albumCover: ImageView


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        trackName = findViewById<TextView>(R.id.track_name)
        artistName = findViewById<TextView>(R.id.artist_name)
        trackDuration = findViewById<TextView>(R.id.trackDuration)
        trackAlbum = findViewById<TextView>(R.id.trackAlbum)
        album = findViewById<TextView>(R.id.album)
        trackYear = findViewById<TextView>(R.id.trackYear)
        trackGenre = findViewById<TextView>(R.id.trackGenre)
        trackCountry = findViewById<TextView>(R.id.trackCountry)
        albumCover = findViewById<ImageView>(R.id.track_image)
        trackDurationPlay = findViewById<TextView>(R.id.durationTrackPlay)




        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val track = intent.getParcelableExtra(TRACK, Track::class.java)  as Track
        goToPlayer(track)

    }

    private fun goToPlayer(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackDurationPlay.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        if (track.collectionName.isNullOrEmpty()) {
            trackAlbum.visibility = View.GONE
            album.visibility = View.GONE
        } else {
            trackAlbum.text = track.collectionName
        }

        trackYear.text = track.getReleaseDateOnlyYear()
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country
        Glide.with(albumCover)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.album_radius)))
            .into(albumCover)
    }

}