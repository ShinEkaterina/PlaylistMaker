package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val track  = tracks[position]

        holder.itemView.setOnClickListener {
            addTrack(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}