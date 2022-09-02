package com.bbj.kinono.data.models

class MovieCastModel : ArrayList<PersonModelItem>()

data class PersonModelItem(
    val description: String,
    val nameEn: String,
    val nameRu: String,
    val posterUrl: String,
    val professionKey: String,
    val professionText: String,
    val staffId: Int
)