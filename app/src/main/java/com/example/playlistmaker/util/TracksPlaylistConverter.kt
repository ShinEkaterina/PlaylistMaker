package com.example.playlistmaker.util

import androidx.room.TypeConverter
import com.example.playlistmaker.common.data.db.entity.TrackEntity

object TracksPlaylistConverter {
    @TypeConverter
    fun fromTrackEntity(trackEntity: TrackEntity): String =
        "${trackEntity.id};" +
                "${trackEntity.name};" +
                "${trackEntity.artistName};" +
                "${trackEntity.trackTimeMillis};" +
                "${trackEntity.artworkUrl};" +
                "${trackEntity.collectionName};" +
                "${trackEntity.releaseDate};" +
                "${trackEntity.genreName};" +
                "${trackEntity.country};" +
                trackEntity.previewUrl

    @TypeConverter
    fun toTrackEntity(data: String): TrackEntity {
        val trackMassive = data.split(";")

        if (trackMassive.size < 10) {
            // Обработка ошибки: данные некорректны или неполны
            throw IllegalArgumentException("Некорректные данные: $data")
        }
        return TrackEntity(
            trackMassive[0].toLong(),
            trackMassive[1],
            trackMassive[2],
            trackMassive[3].toLong(),
            trackMassive[4],
            trackMassive[5],
            trackMassive[6],
            trackMassive[7],
            trackMassive[8],
            trackMassive[9]
        )
    }
}