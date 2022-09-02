package com.bbj.kinono.data.models

data class MovieFactModel(
    val items: List<FactItem>,
    val total: Int
)

data class FactItem(
    val spoiler: Boolean,
    val text: String,
    val type: String
)