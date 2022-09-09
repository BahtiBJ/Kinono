package com.bbj.kinono.data.models

import com.bbj.kinono.data.models.common.Country
import com.bbj.kinono.data.models.common.Genre

data class SearchResultModel(
    val items: List<FoundedFilm>,
    val total: Int,
    val totalPages: Int
)

data class FoundedFilm(
    val countries: List<Country>,
    val genres: List<Genre>,
    val imdbId: String,
    val kinopoiskId: Int,
    val nameEn: Any,
    val nameOriginal: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val ratingImdb: Double,
    val ratingKinopoisk: Double,
    val type: String,
    val year: Int
){

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
        for (genre in genres){
            genresString += "${genre.genre},"
        }
        return genresString.trim().dropLast(1)

    }
}



