package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.TrackRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TrackRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.errorCode, resource.message)
                }
            }
        }
    }
}