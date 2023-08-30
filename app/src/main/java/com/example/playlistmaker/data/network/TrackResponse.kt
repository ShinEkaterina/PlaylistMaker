package com.example.playlistmaker.data.network

import com.example.playlistmaker.domain.model.Track

data class TrackResponse(val resultCount: Int, val results: ArrayList<Track>)

