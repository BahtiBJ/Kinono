package com.bbj.kinono.di

import com.bbj.kinono.view.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        MainViewModel(
            getPremiereUseCase = get(),
            getPopularUseCase = get(),
            getMovieDetailUseCase = get(),
            getMovieCastUseCase = get(),
            getMovieFactUseCase = get(),
            searchUseCase = get()
        )
    }

}