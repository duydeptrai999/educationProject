package com.example.asm2f.data.retrofit

import com.example.asm2f.data.model.CastResponse
import com.example.asm2f.data.model.GenresResults
import com.example.asm2f.data.model.Movie
import com.example.asm2f.data.model.MovieResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiRetrofitService {
    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieResult>

    @GET("movie/top_rated")
    suspend fun getTopRate(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieResult>

    @GET("movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<Movie>

    @GET("movie/{movieId}/credits")
    fun getMovieCredits(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<CastResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieResult>

    @GET("movie/upcoming")
    suspend fun getUpComing(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieResult>


    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Response<MovieResult>

    @GET("/3/genre/movie/list?language=en")
    suspend fun getGenres(
        @Query("api_key") apiKey: String,
    ): Response<GenresResults>

    @GET("/3/discover/movie?language=en-US&page=1&sort_by=popularity.desc")
    suspend fun getMoviesOfGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String
    ): Response<MovieResult>


}
