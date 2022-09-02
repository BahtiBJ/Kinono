package com.bbj.kinono.domain

import com.bbj.kinono.data.models.PopularModel

class GetPopularUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute(page : Int = 1) : PopularModel {
        return cinemaRepository.getPopular(page)
    }

}