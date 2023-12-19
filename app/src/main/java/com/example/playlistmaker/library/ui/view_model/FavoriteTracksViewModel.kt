package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.model.Track
import com.example.playlistmaker.library.domain.db.FavoriteTracksInteractor
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    private val state = MutableLiveData<FavTracksFragmentState>()
    fun observeState(): LiveData<FavTracksFragmentState> = state

    init {
        fillData()
    }

    fun fillData() {
        state.postValue(FavTracksFragmentState.LOADING)
        viewModelScope.launch {
            favoriteTracksInteractor.getAll().collect { favorites ->
                processResult(favorites)
            }
        }
    }

    private fun processResult(favorites: List<Track>) {
        if (favorites.isEmpty()) {
            state.postValue(FavTracksFragmentState.NO_FAVORITE_TRACKS)
        } else {
            state.postValue(FavTracksFragmentState.FAVORITES(favorites))
        }
    }
}