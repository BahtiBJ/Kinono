package com.bbj.kinono.domain

import com.bbj.kinono.data.models.PremiereModel

class GetPremiereUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute() : PremiereModel {
        return cinemaRepository.getPremiere()
    }

}