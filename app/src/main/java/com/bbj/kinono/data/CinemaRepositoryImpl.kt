package com.bbj.kinono.data

import com.bbj.kinono.data.models.*
import com.bbj.kinono.data.retrofit.NetworkDAO
import com.bbj.kinono.domain.CinemaRepository
import java.time.Month
import java.util.*

class CinemaRepositoryImpl(private val networkDAO: NetworkDAO) : CinemaRepository {

    private val calendar = Calendar.getInstance()
    private val today =
        Pair(
            calendar.get(Calendar.YEAR), Month.of(calendar.get(Calendar.MONTH))
        )

    private var savedMovieDetail: Pair<Int, MovieDetailModel>? = null
    private var savedMovieCast: Pair<Int, MovieCastModel>? = null
    private var savedMovieFacts: Pair<Int, MovieFactModel>? = null
    private var savedSearchResult: Triple<String, Int, SearchResultModel>? = null

    override suspend fun getPopular(page: Int): PopularModel {
        val response = networkDAO.getPopular(page)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getPremiere(): PremiereModel {
        val response = networkDAO.getPremiere(today.first, today.second)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getMovieDetail(id: Int): MovieDetailModel {
        savedMovieDetail?.let {
            if (id == savedMovieDetail?.first)
                return savedMovieDetail!!.second
        }
        val response = networkDAO.getMovieById(id)
        if (response.isSuccessful) {
            val movieDetail = response.body()!!
            savedMovieDetail = Pair(id, movieDetail)
            return movieDetail
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getMovieCast(id: Int): MovieCastModel {
        savedMovieCast?.let {
            if (id == savedMovieCast?.first)
                return savedMovieCast!!.second
        }
        val response = networkDAO.getCast(id)
        if (response.isSuccessful) {
            val movieCast = response.body()!!
            savedMovieCast = Pair(id, movieCast)
            return movieCast
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getMovieFact(id: Int): MovieFactModel {
        savedMovieFacts?.let {
            if (id == savedMovieFacts?.first)
                return savedMovieFacts!!.second
        }
        val response = networkDAO.getMovieFactsById(id)
        if (response.isSuccessful) {
            val movieFacts = response.body()!!
            savedMovieFacts = Pair(id, movieFacts)
            return movieFacts
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun searchMovie(
        keyword: String,
        page: Int,
        countries: Int?,
        genres: Int?,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int
    ): SearchResultModel {
        val response = networkDAO.searchByKeyword(
            keyword,
            page,
            countries,
            genres,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo
        )
        if (response.isSuccessful) {
            val searchResult = response.body()!!
            return searchResult
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getFavorite(): MovieDetailModel {
        TODO("Not yet implemented")
    }

    override suspend fun saveToFavorite(): MovieDetailModel {
        TODO("Not yet implemented")
    }
}