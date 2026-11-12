package com.nicolas.peliculasapi.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PeliculasAPI {

    @GET("/")
    suspend fun getPeliculasList(
        @Query("s") title: String,
        @Query("apikey") apiKey: String = API_KEY,
    ): PeliculasResponse

    @GET("/")
    suspend fun getPeliculaByID(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = API_KEY,
    ): Pelicula

    companion object {
        private const val BASE_URL = "https://www.omdbapi.com/"
        const val API_KEY = "8f2cf63c"

        fun getInstance(): PeliculasAPI {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(PeliculasAPI::class.java)
        }
    }
}
