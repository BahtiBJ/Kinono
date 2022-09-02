package com.bbj.kinono.domain

import com.bbj.kinono.data.models.MovieDetailModel
import com.bbj.kinono.data.models.MovieFactModel

class GetMovieFactUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute(id : Int) : MovieFactModel {
        return cinemaRepository.getMovieFact(id)
    }

}