package com.bbj.kinono.domain

import com.bbj.kinono.domain.models.CastingModel

class GetCastUseCase (val cinemaRepository: CinemaRepository) {

    private var savedMovieCast: Pair<Int, List<CastingModel>>? = null

    suspend fun execute(id : Int) : List<CastingModel> {
        savedMovieCast?.let {
            if (id == savedMovieCast?.first)
                return savedMovieCast!!.second
        }
        val movieCast = cinemaRepository.getMovieCast(id)
        savedMovieCast = Pair(id, movieCast)
        return movieCast
    }

}