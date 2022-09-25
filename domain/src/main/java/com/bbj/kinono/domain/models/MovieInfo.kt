package com.bbj.kinono.domain.models

data class MovieInfo(
    val id: Int,
    val movieName: String,
    val posterURL: String,
    val movieYear: String,
    val movieCountry: String,
    val movieGenre: String,
    val movieRating: String,
    val movieAgeLimit: String,
    val movieDuration: String,
    val movieDescribtion: String
)

