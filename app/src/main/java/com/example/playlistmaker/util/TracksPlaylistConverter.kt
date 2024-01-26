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
        val trackMassive = data?.split(";")
        return TrackEntity(
            trackMassive?.get(0)!!.toString().toLong(),
            trackMassive?.get(1).toString(),
            trackMassive?.get(2).toString(),
            trackMassive?.get(3).toString().toLong(),
            trackMassive?.get(4).toString(),
            trackMassive?.get(5).toString(),
            trackMassive?.get(6).toString(),
            trackMassive?.get(7).toString(),
            trackMassive?.get(8).toString(),
            trackMassive?.get(9).toString(),
        )
    }
}