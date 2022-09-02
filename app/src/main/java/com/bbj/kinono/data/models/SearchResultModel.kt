package com.bbj.kinono.data.models

import com.bbj.kinono.data.models.common.Country
import com.bbj.kinono.data.models.common.Genre

data class SearchResultModel(
    val films: List<FoundedFilm>,
    val keyword: String,
    val pagesCount: Int,
    val searchFilmsCountResult: Int
)

data class FoundedFilm(
    val countries: List<Country>,
    val description: String,
    val filmId: Int,
    val filmLength: String,
    val genres: List<Genre>,
    val nameEn: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val rating: String,
    val ratingVoteCount: Int,
    val type: String,
    val year: String
) {
    fun getDuration() : String {
        val duration = filmLength.split(":")
        return duration[0] + "ч" + duration[1] + "мин"
    }

    fun getGenres() : String{
        var genresString : String = ""
        for (genre in genres){
            genresString += "${genre.genre},"
        }
        return genresString.trim().dropLast(1)

    }
}

