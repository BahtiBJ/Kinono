package com.bbj.kinono.domain.models

data class SearchResult(val items : List<FoundedMovie>, val totalPages : Int)

data class FoundedMovie(
    val id : Int,
    val name: String?,
    val year: String,
    val posterURL: String,
    val rating: String,
    val genres: String,
    val countries: String
)