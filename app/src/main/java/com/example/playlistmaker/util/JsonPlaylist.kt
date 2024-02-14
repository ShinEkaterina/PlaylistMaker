package com.example.playlistmaker.util

import com.example.playlistmaker.common.domain.model.Playlist
import com.example.playlistmaker.library.ui.PlaylistInfo
import com.google.gson.Gson

fun createJsonFromPlaylist(playlist: Playlist): String {
    val playlistInfo = PlaylistMapper.map(playlist)
    return Gson().toJson(playlistInfo)
}

fun createPlaylistFromJson(json: String?): Playlist {
    val playlistInfo = Gson().fromJson(json, PlaylistInfo::class.java)
    return PlaylistMapper.map(playlistInfo)
}

