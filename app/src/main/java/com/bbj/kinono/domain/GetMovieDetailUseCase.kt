package com.bbj.kinono.domain

import com.bbj.kinono.data.models.MovieDetailModel

class GetMovieDetailUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute(id : Int) : MovieDetailModel{
        return cinemaRepository.getMovieDetail(id)
    }

}