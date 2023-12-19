package com.example.playlistmaker.library.ui.view_model

import com.example.playlistmaker.common.domain.model.Track

sealed class FavTracksFragmentState{
    object NO_FAVORITE_TRACKS : FavTracksFragmentState()
    object LOADING : FavTracksFragmentState()
    data class FAVORITES(val favorites: List<Track>) : FavTracksFragmentState()
}
