package com.example.playlistmaker.library.ui.playlists_grid.fragment

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.common.presentation.rightEndingTrack
import com.example.playlistmaker.util.dpToPx


class PlaylistViewHolder(
    parent: ViewGroup,
    layoutRes: Int,
    private val clickListener: PlaylistAdapter.PlaylistClickListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(layoutRes, parent, false)
) {

    private val playlistName: TextView = itemView.findViewById(R.id.tvPlaylistName)
    private val countSongs: TextView = itemView.findViewById(R.id.tvCountSongs)
    private val imagePlaylist: ImageView = itemView.findViewById(R.id.ivPlaylist)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.name
        val count =
            if (playlist.tracks.isNullOrEmpty())
                0
            else
                playlist.tracks.size
        countSongs.text = rightEndingTrack(count)
        showImage(playlist.imageUri)

        itemView.setOnClickListener { clickListener.onClick(playlist) }
    }

    private fun showImage(uri: Uri?) {
        Glide.with(itemView.context)
            .load(uri)
            .placeholder(R.drawable.playlist_card)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2.0F, itemView.context)))
            .into(imagePlaylist)
    }
}