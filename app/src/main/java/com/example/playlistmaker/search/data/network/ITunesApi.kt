package com.example.playlistmaker.search.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun searchTracks(@Query("term") term: String): TrackSearchResponse
}