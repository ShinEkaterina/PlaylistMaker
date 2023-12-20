package com.example.playlistmaker.util

import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.util.Formater

class TrackDbMapper {
    fun map(track: TrackEntity): Track {
        return Track(
            trackId = track.id,
            trackName = track.name,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.genreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = true
        )
    }
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            id = track.trackId,
            name = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl = track.getCoverArtwork(),
            collectionName = track.collectionName,
            releaseDate = track.getReleaseDateOnlyYear(),
            genreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
}