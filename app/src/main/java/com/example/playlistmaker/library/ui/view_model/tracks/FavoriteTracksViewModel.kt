package com.example.playlistmaker.library.ui.view_model.tracks

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
    private val _state = MutableLiveData<FavTracksFragmentState>()
    fun observeState(): LiveData<FavTracksFragmentState> = _state

    init {
        fillData()
    }

    fun fillData() {
        _state.postValue(FavTracksFragmentState.Loading)
        viewModelScope.launch {
            favoriteTracksInteractor.getAll().collect { favorites ->
                processResult(favorites)
            }
        }
    }

    private fun processResult(favorites: List<Track>) {
        if (favorites.isEmpty()) {
            _state.postValue(FavTracksFragmentState.NoFavoriteTracks)
        } else {
            _state.postValue(FavTracksFragmentState.Favorites(favorites))
        }
    }
}