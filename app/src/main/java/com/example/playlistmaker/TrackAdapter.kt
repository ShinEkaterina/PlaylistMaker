package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val data: ArrayList<Track>) :
    RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val item: View =
            LayoutInflater.from(parent.context).inflate(
                    R.layout.track_view_card,
                parent,
                false
            )
        return TrackViewHolder(item)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
    }


    override fun getItemCount(): Int {
        return data.size
    }
}