package com.bbj.kinono.data

import com.bbj.kinono.data.retrofit.NetworkDAO
import com.bbj.kinono.domain.CinemaRepository
import com.bbj.kinono.domain.models.*
import java.time.Month
import java.util.*

class CinemaRepositoryImpl(private val networkDAO: NetworkDAO) : CinemaRepository {

    private val calendar = Calendar.getInstance()
    private val today =
        Pair(
            calendar.get(Calendar.YEAR), Month.of(calendar.get(Calendar.MONTH))
        )


    override suspend fun getPopular(page: Int): PreviewListModel {
        val response = networkDAO.getPopular(page)
        if (response.isSuccessful) {
            val resultResponse = response.body()!!
            val popularList = resultResponse.films.map {
                PreviewModel(
                    it.filmId,
                    it.nameRu,
                    it.year,
                    it.posterUrl,
                    it.rating,
                    it.getDuration(),
                    it.getGenres()
                )
            }
            return PreviewListModel(popularList,resultResponse.pagesCount)
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getPremiere(): List<PosterInfo> {
        val response = networkDAO.getPremiere(today.first, today.second)
        if (response.isSuccessful) {
            val premiereList =
                response.body()!!.items.map { PosterInfo(it.kinopoiskId, it.posterUrl) }
            return premiereList
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getMovieDetail(id: Int): MovieInfo {
        val response = networkDAO.getMovieById(id)
        if (response.isSuccessful) {
            val movieDetail = response.body()!!
            movieDetail.run {
                return MovieInfo(kinopoiskId,
                    nameRu ?: "",
                    posterUrl ?: "",
                    year.toString() ?: "",
                    getCountryListString() ?: "",
                    getGenres() ?: "",
                    ratingKinopoisk.toString() ?: "",
                    ratingAgeLimits ?: "",
                    filmLength.toString() ?: "",
                    description ?: ""
                )
            }
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getMovieCast(id: Int): List<CastingModel> {
        val response = networkDAO.getCast(id)
        if (response.isSuccessful) {
            val movieCast = response.body()!!
            return movieCast.map {
                CastingModel(
                    it.nameRu,
                    it.posterUrl,
                    it.professionText,
                    it.description ?: ""
                )
            }

        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getMovieFact(id: Int): List<FactsModel> {
        val response = networkDAO.getMovieFactsById(id)
        if (response.isSuccessful) {
            val movieFacts = response.body()!!
            return movieFacts.items.map { FactsModel(it.text) }
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
    ): SearchResult {
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
            val foundedMovie = searchResult.items.map {
                FoundedMovie(
                    it.kinopoiskId,
                    it.nameRu ?: "",
                    it.year.toString(),
                    it.posterUrl,
                    it.ratingKinopoisk.toString(),
                    it.getGenres(),
                    it.getCountryListString()
                )
            }
            return SearchResult(foundedMovie,searchResult.totalPages)
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override suspend fun getFavorite(): List<MovieInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun saveToFavorite(): MovieInfo {
        TODO("Not yet implemented")
    }
}