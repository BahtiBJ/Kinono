package com.bbj.kinono.domain

import com.bbj.kinono.data.models.*

interface CinemaRepository {

    suspend fun getPopular(page: Int = 1): PopularModel

    suspend fun getPremiere(): PremiereModel

    suspend fun getMovieDetail(id: Int): MovieDetailModel

    suspend fun getMovieCast(id : Int) : MovieCastModel

    suspend fun getMovieFact(id : Int) : MovieFactModel

    suspend fun searchMovie(
        keyword: String,
        page: Int,
        countries: Int?,
        genres: Int?,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int
    ): SearchResultModel

    suspend fun getFavorite(): MovieDetailModel

    suspend fun saveToFavorite(): MovieDetailModel


}