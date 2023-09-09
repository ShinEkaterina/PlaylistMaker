package com.example.playlistmaker.presentation

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.player.model.TrackInfo

object Mapper {
    fun mapTrackToTrackInfo(track: Track): TrackInfo {
        return TrackInfo(
            id = track.trackId,
            name = track.trackName,
            artistName = track.artistName,
            duration = Formater.formayMillsTimeToDuration(track.trackTimeMillis),
            artworkUrl = track.getCoverArtwork(),
            collectionName = track.collectionName,
            releaseYear = track.getReleaseDateOnlyYear(),
            genreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
}