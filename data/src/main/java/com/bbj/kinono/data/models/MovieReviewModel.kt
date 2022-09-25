package com.bbj.kinono.data.models

data class MovieReviewModel(
    val items: List<ReviewItem>,
    val total: Int,
    val totalNegativeReviews: Int,
    val totalNeutralReviews: Int,
    val totalPages: Int,
    val totalPositiveReviews: Int
)

data class ReviewItem(
    val author: String,
    val date: String,
    val description: String,
    val kinopoiskId: Int,
    val negativeRating: Int,
    val positiveRating: Int,
    val title: String,
    val type: String
)