package com.example.playlistmaker

import android.content.Intent
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.SearchHistory.addTrack


class TrackAdapter() :
    RecyclerView.Adapter<TrackViewHolder>() {
    var tracks: ArrayList<Track> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        val track = tracks[position]

        holder.itemView.setOnClickListener {
            addTrack(track)
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(App.TRACK, track)
            startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}