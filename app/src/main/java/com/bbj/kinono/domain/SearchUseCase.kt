package com.bbj.kinono.domain

import com.bbj.kinono.data.models.SearchResultModel

class SearchUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute(keyword : String, page : Int = 1) : SearchResultModel{
        return cinemaRepository.searchMovie(keyword, page)
    }

}