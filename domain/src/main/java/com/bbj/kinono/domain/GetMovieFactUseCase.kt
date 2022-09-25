package com.bbj.kinono.domain

import com.bbj.kinono.domain.models.FactsModel

class GetMovieFactUseCase (val cinemaRepository: CinemaRepository) {

    private var savedMovieFacts: Pair<Int, List<FactsModel>>? = null

    suspend fun execute(id : Int) : List<FactsModel> {
        savedMovieFacts?.let {
            if (id == savedMovieFacts?.first)
                return savedMovieFacts!!.second
        }
        val facts = cinemaRepository.getMovieFact(id)
        savedMovieFacts = Pair(id, facts)
        return facts
    }

}