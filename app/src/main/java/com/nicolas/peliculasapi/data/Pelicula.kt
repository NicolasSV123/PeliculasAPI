package com.nicolas.peliculasapi.data

import com.google.gson.annotations.SerializedName

data class PeliculasResponse(
    @SerializedName("Search") val peliculas: List<Pelicula>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val response: String?,
    @SerializedName("Error") val error: String?,
)

data class Pelicula(
    @SerializedName("Title") val title: String? = null,
    @SerializedName("Year") val year: String? = null,
    @SerializedName("Poster") val poster: String? = null,
    @SerializedName("imdbID") val imdbID: String? = null,
    @SerializedName("Plot") val plot: String? = null,
    @SerializedName("Runtime") val runtime: String? = null,
    @SerializedName("Director") val director: String? = null,
    @SerializedName("Genre") val genre: String? = null,
    @SerializedName("Country") val country: String? = null,
)
