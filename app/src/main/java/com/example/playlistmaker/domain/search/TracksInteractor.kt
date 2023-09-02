package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track

interface TracksInteractor {

    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>)
    }
}