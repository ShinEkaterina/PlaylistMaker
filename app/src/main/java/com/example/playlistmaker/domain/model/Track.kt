package com.example.playlistmaker.domain.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Int,
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
    fun getReleaseDateOnlyYear(): String {
        val answer = releaseDate.split("-")
        return answer[0]
    }

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

}

