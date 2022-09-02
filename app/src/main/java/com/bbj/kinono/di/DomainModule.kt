package com.bbj.kinono.di

import com.bbj.kinono.domain.*
import org.koin.dsl.module

val domainModule = module {

    factory {
        GetMovieDetailUseCase(get())
    }

    factory {
        GetMovieCastUseCase(get())
    }

    factory {
        GetMovieFactUseCase(get())
    }

    factory {
        GetPopularUseCase(get())
    }

    factory {
        GetPremiereUseCase(get())
    }

    factory {
        SearchUseCase(get())
    }
}