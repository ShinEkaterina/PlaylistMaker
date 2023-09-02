package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.TrackRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import java.util.concurrent.Executors

class TracksInteractorImpl (private val repository: TrackRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}