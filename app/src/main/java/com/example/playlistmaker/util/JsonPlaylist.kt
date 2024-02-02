package com.example.playlistmaker.util

import com.example.playlistmaker.common.domain.model.Playlist
import com.google.gson.Gson

fun createJsonFromPlaylist(playlist: Playlist) = Gson().toJson(playlist)

fun createPlaylistFromJson(json: String?) = Gson().fromJson(json, Playlist::class.java)