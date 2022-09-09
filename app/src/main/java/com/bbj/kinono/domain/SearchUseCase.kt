package com.bbj.kinono.domain

import com.bbj.kinono.data.models.SearchResultModel

class SearchUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute(keyword: String,
                        page: Int,
                        countries: Int?,
                        genres: Int?,
                        ratingFrom: Int,
                        ratingTo: Int,
                        yearFrom: Int,
                        yearTo: Int) : SearchResultModel{
        return cinemaRepository.searchMovie(keyword,
            page,
            countries,
            genres,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo)
    }

}