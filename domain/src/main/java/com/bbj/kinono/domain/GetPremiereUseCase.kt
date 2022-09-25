package com.bbj.kinono.domain

import com.bbj.kinono.domain.models.PosterInfo

class GetPremiereUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute() : List<PosterInfo> {
        return cinemaRepository.getPremiere()
    }

}