package com.example.playlistmaker.search.presentation

import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.player.presentation.model.TrackInfo
import com.example.playlistmaker.util.Formater

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
            previewUrl = track.previewUrl,
            ifFavorite = track.isFavorite
        )
    }
}