package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.model.Track

interface TrackRepository {
    fun searchTracks(expression:String):List<Track>
}