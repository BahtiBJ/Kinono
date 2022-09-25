package com.bbj.kinono.domain.models

data class PreviewListModel(val films : List<PreviewModel>, val pagesCount : Int)

data class PreviewModel(
    val id: Int,
    val name: String,
    val year: String,
    val posterURL: String,
    val rating: String,
    val duration: String,
    val genres: String
)

