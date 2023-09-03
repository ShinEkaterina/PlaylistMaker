package com.example.playlistmaker.ui.search


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewCardBinding
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale


class TrackAdapter(private val listener: (Track) -> Unit) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    var tracks: ArrayList<Track> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = TrackViewCardBinding.inflate(layoutInspector, parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { listener(track) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

     class TrackViewHolder(private val binding: TrackViewCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Track) {
            binding.trackName.text = model.trackName
            binding.artistName.text= model.artistName
            binding.trackTime.text =
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
                .into(binding.trackImage)

        }
    }
}