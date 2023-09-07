package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTracks(expression:String): Resource<List<Track>>
}