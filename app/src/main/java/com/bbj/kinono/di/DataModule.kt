package com.bbj.kinono.di

import com.bbj.kinono.data.CinemaRepositoryImpl
import com.bbj.kinono.data.retrofit.NetworkDAO
import com.bbj.kinono.domain.CinemaRepository
import com.simplemented.okdelay.DelayInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val baseUrl = "https://kinopoiskapiunofficial.tech/api/"
const val apiKey = "95261db9-8cfa-4497-b086-eed559dd4bc0"

val dataModule = module {

    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        provideRetrofit(get()).create(NetworkDAO::class.java)
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor(DelayInterceptor(1,TimeUnit.SECONDS))
            .addNetworkInterceptor {chain ->
                val requestBuilder : Request.Builder = chain.request().newBuilder()
                requestBuilder.addHeader("accept","application/json")
                requestBuilder.addHeader("X-API-KEY", apiKey)
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    single<CinemaRepository> {
        CinemaRepositoryImpl(get())
    }
}