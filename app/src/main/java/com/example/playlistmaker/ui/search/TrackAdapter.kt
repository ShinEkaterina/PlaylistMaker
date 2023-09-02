package com.example.playlistmaker.ui.search


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.model.Track


class TrackAdapter(private val listener: (Track) -> Unit) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var tracks: ArrayList<Track> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { listener(track) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}