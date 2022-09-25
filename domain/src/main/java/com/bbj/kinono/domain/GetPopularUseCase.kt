package com.bbj.kinono.domain

import com.bbj.kinono.domain.models.PreviewListModel
import com.bbj.kinono.domain.models.PreviewModel

class GetPopularUseCase (val cinemaRepository: CinemaRepository) {

    suspend fun execute(page : Int = 1) : PreviewListModel {
        return cinemaRepository.getPopular(page)
    }

}