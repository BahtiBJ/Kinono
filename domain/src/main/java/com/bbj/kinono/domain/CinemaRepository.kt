package com.bbj.kinono.domain

import com.bbj.kinono.domain.models.*

interface CinemaRepository {

    suspend fun getPopular(page: Int = 1): PreviewListModel

    suspend fun getPremiere(): List<PosterInfo>

    suspend fun getMovieDetail(id: Int): MovieInfo

    suspend fun getMovieCast(id : Int) : List<CastingModel>

    suspend fun getMovieFact(id : Int) : List<FactsModel>

    suspend fun searchMovie(
        keyword: String,
        page: Int,
        countries: Int?,
        genres: Int?,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int
    ): SearchResult

    suspend fun getFavorite(): List<MovieInfo>

    suspend fun saveToFavorite(): MovieInfo


}