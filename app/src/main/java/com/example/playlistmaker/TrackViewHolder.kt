package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class TrackViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {
    private var imageAlbum: ImageView = parentView.findViewById(R.id.track_image)
    private var trackName: TextView = parentView.findViewById(R.id.track_name)
    private var artistName: TextView = parentView.findViewById(R.id.artist_name)
    private var trackTime: TextView = parentView.findViewById(R.id.track_time)


    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)
/*
        Glide
            .with(model.context)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    itemView.resources
                        .getDimensionPixelOffset(R.dimen.image_radius)
                )
            )
            .into(imageAlbum)

    }

 */
    }
}