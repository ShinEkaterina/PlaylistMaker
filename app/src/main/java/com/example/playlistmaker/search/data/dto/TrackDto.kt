package com.example.playlistmaker.search.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackDto(
    val trackId: Long,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String,// Название альбома
    val releaseDate: String,// Год релиза трека
    val primaryGenreName: String,// Жанр трека
    val country: String, // Страна исполнителя
    val previewUrl: String //ссылка на отрывок трека для прослушивания
) : Parcelable {

}