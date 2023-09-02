package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale


class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.track_view_card, parentView, false)
) {
    private var imageAlbum: ImageView = itemView.findViewById(R.id.track_image)
    private var trackName: TextView = itemView.findViewById(R.id.track_name)
    private var artistName: TextView = itemView.findViewById(R.id.artist_name)
    private var trackTime: TextView = itemView.findViewById(R.id.track_time)


    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)

        Glide
            .with(itemView.context)
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


}
