package com.example.playlistmaker.data.search.network

import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    suspend fun searchTracks(@Query("term") term: String): TrackSearchResponse
}