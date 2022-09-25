package com.bbj.kinono.data.retrofit

import com.bbj.kinono.data.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.Month

interface NetworkDAO {

    @GET("v2.2/films/premieres")
    suspend fun getPremiere(
        @Query("year") year: Int,
        @Query("month") month: Month
    ): Response<PremiereModel>

    @GET("v2.2/films/top?type=TOP_250_BEST_FILMS")
    suspend fun getPopular(@Query("page") page: Int): Response<PopularModel>

    @GET("v2.2/films/{id}")
    suspend fun getMovieById(@Path("id") id: Int): Response<MovieDetailModel>

    @GET("v2.2/films/{id}/facts")
    suspend fun getMovieFactsById(@Path("id") id: Int): Response<MovieFactModel>

    @GET("v1/staff")
    suspend fun getCast(@Query("filmId") id: Int): Response<MovieCastModel>

    @GET("v2.2/films?type=ALL&order=RATING")
    suspend fun searchByKeyword(
        @Query("keyword") keyword: String,
        @Query("page") page: Int,
        @Query("countries") countries: Int?,
        @Query("genres") genres: Int?,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int
    ): Response<SearchResultModel>

    @GET("v2.2/films/{id}/reviews")
    suspend fun getMovieReviewsById(@Path("id") id: Int): Response<MovieFactModel>
}