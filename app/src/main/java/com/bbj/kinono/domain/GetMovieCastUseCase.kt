package com.bbj.kinono.domain

import com.bbj.kinono.data.models.MovieCastModel

class GetMovieCastUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute(id : Int) : MovieCastModel {
        return cinemaRepository.getMovieCast(id)
    }

}