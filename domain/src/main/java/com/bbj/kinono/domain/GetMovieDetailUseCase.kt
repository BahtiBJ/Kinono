package com.bbj.kinono.domain

import com.bbj.kinono.domain.models.MovieInfo

class GetMovieDetailUseCase (val cinemaRepository: CinemaRepository) {

    private var savedMovieDetail: Pair<Int, MovieInfo>? = null

    suspend fun execute(id : Int) : MovieInfo{
        savedMovieDetail?.let {
            if (id == savedMovieDetail?.first)
                return savedMovieDetail!!.second
        }
        val movieInfo = cinemaRepository.getMovieDetail(id)
        savedMovieDetail = Pair(id, movieInfo)
        return movieInfo
    }

}