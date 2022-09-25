package com.bbj.kinono.data.models

import com.bbj.kinono.data.models.common.Country
import com.bbj.kinono.data.models.common.Genre

data class MovieDetailModel (
    val completed: Boolean,
    val countries: List<Country>,
    val coverUrl: String,
    val description: String,
    val editorAnnotation: String,
    val endYear: Int,
    val filmLength: Int,
    val genres: List<Genre>,
    val has3D: Boolean,
    val hasImax: Boolean,
    val imdbId: String,
    val isTicketsAvailable: Boolean,
    val kinopoiskId: Int,
    val lastSync: String,
    val logoUrl: String,
    val nameEn: String,
    val nameOriginal: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val productionStatus: String,
    val ratingAgeLimits: String,
    val ratingAwait: Double,
    val ratingAwaitCount: Int,
    val ratingFilmCritics: Double,
    val ratingFilmCriticsVoteCount: Int,
    val ratingGoodReview: Double,
    val ratingGoodReviewVoteCount: Int,
    val ratingImdb: Double,
    val ratingImdbVoteCount: Int,
    val ratingKinopoisk: Double,
    val ratingKinopoiskVoteCount: Int,
    val ratingMpaa: String,
    val ratingRfCritics: Double,
    val ratingRfCriticsVoteCount: Int,
    val reviewsCount: Int,
    val serial: Boolean,
    val shortDescription: String,
    val shortFilm: Boolean,
    val slogan: String,
    val startYear: Int,
    val type: String,
    val webUrl: String,
    val year: Int
) {

    fun getCountryListString() : String{
        var countryString = ""
        for (i in 0 until countries.size){
            val add = if ((i+1) % 2 == 0) "\n" else ""
            countryString += countries[i].country + "," + add
        }
        return countryString.trim().dropLast(1)
    }

    fun getGenres() : String{
        var genresString : String = ""
        for (i in 0 until genres.size){
            val add = if ((i+1) % 2 == 0) "\n" else ""
            genresString += genres[i].genre + "," + add
        }
        return genresString.trim().dropLast(1)
    }
}
