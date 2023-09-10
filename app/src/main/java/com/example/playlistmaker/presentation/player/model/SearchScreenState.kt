package com.example.playlistmaker.presentation.player.model

import com.example.playlistmaker.domain.model.Track


data class SearchScreenState(

    val tracks: List<Track>,
    val isLoading: Boolean,
    /*    val placeholderMessage: Int?,
        val needToUpdate:Boolean,*/
    val errorType: ErrorType?,
    val toShowHistory:Boolean,
    val history: List<Track>

)