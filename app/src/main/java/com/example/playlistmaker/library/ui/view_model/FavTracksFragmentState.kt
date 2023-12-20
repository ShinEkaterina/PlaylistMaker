package com.example.playlistmaker.library.ui.view_model

import com.example.playlistmaker.common.domain.model.Track

sealed class FavTracksFragmentState {
    object NoFavoriteTracks : FavTracksFragmentState()
    object Loading : FavTracksFragmentState()
    data class Favorites(val favorites: List<Track>) : FavTracksFragmentState()
}
