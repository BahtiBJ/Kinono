package com.bbj.kinono.data.models

import com.bbj.kinono.data.models.common.Country
import com.bbj.kinono.data.models.common.Genre

data class PremiereModel(
    val items: List<Item>,
    val total: Int
)

data class Item(
    val countries: List<Country>,
    val duration: Int,
    val genres: List<Genre>,
    val kinopoiskId: Int,
    val nameEn: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val premiereRu: String,
    val year: Int
)
