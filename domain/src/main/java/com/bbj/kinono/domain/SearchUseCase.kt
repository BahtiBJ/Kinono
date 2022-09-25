package com.bbj.kinono.domain

import com.bbj.kinono.domain.models.FoundedMovie
import com.bbj.kinono.domain.models.SearchResult

class SearchUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute(keyword: String,
                        page: Int,
                        countries: Int?,
                        genres: Int?,
                        ratingFrom: Int,
                        ratingTo: Int,
                        yearFrom: Int,
                        yearTo: Int) : SearchResult {
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