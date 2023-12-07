package com.example.playlistmaker.library.data.db.mapper

import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.player.presentation.model.TrackInfo

class TrackDbMapper {
    fun map(track: TrackEntity): TrackInfo {
        return TrackInfo(
            id = track.id,
            name = track.name,
            artistName = track.artistName,
            duration = track.duration,
            artworkUrl = track.artworkUrl,
            collectionName = track.collectionName,
            releaseYear = track.releaseYear,
            genreName = track.genreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
}