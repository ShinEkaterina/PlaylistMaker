package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.TrackDto

class TrackSearchResponse (val resultCount: Int,
                           val expression: String,
                           val results: List<TrackDto>) : Response()