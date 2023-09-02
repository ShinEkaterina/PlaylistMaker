package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.model.Track

class TrackSearchResponse (val resultCount: Int,
                           val expression: String,
                           val results: List<TrackDto>) : Response()