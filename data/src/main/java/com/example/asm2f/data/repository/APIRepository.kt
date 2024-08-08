package com.example.asm2f.data.repository

import com.example.asm2f.data.model.Genres
import com.example.asm2f.data.model.Movie
import com.example.asm2f.data.retrofit.RetrofitClient
import android.util.Log
import com.example.asm2f.data.Resource
import com.example.asm2f.data.model.GenresResults

import retrofit2.Response

class APIRepository {
    private  val API_KEY = "d5b97a6fad46348136d87b78895a0c06"

    suspend fun searchMovie(queryString: String): Resource<List<Movie>> {
        return try {
            val response = RetrofitClient.api.searchMovies(API_KEY, queryString)
            if (response.isSuccessful) {
                Resource.success(response.body()?.results ?: emptyList())
            } else {
                Resource.error("Failed to fetch moviesSearch: ${response.message()}", null)
            }
        } catch (e: Exception) {
            Resource.error("An error occurred: ${e.message}", null)
        }
    }

    suspend fun getGenres(): Resource<List<Genres>> {

        return try {
            val response = RetrofitClient.api.getGenres(API_KEY)
            if (response.isSuccessful){
//                Log.d("RetrofitClient", "API Response: ${response.body()}")
                Resource.success(response.body()?.genres ?: emptyList())
            }else {
                Resource.error("Failed to fetch moviesGenres: ${response.message()}", null)
            }
        }catch (e:Exception){
            Resource.error("An error occurred: ${e.message}", null)
        }
    }

    suspend fun getGenresOfMovie(genreId:String): Resource<List<Movie>> {

        return try {
            val response = RetrofitClient.api.getMoviesOfGenre(API_KEY,genreId)
            if (response.isSuccessful){
//                Log.d("RetrofitClient", "API Response: ${response.body()}")
                Resource.success(response.body()?.results ?: emptyList())
            }else {
                Resource.error("Failed to fetch moviesPopular: ${response.message()}", null)
            }
        }catch (e:Exception){
            Resource.error("An error occurred: ${e.message}", null)
        }
    }
    suspend fun getPopularMovie(page : Int): Resource<List<Movie>> {

        return try {
            val response = RetrofitClient.api.getPopular(API_KEY,page)
            if (response.isSuccessful){
//                Log.d("RetrofitClient", "API Response: ${response.body()}")
                Resource.success(response.body()?.results ?: emptyList())
            }else {
                Resource.error("Failed to fetch moviesGenres: ${response.message()}", null)
            }
        }catch (e:Exception){
            Resource.error("An error occurred: ${e.message}", null)
        }
    }

    suspend fun getNowPlaying(page : Int): Resource<List<Movie>> {

        return try {
            val response = RetrofitClient.api.getNowPlaying(API_KEY,page)
            if (response.isSuccessful){

                Resource.success(response.body()?.results ?: emptyList())
            }else {
                Resource.error("Failed to fetch moviesGenres: ${response.message()}", null)
            }
        }catch (e:Exception){
            Resource.error("An error occurred: ${e.message}", null)
        }
    }
    suspend fun getTopRate(page : Int): Resource<List<Movie>> {

        return try {
            val response = RetrofitClient.api.getTopRate(API_KEY,page)
            if (response.isSuccessful){

                Resource.success(response.body()?.results ?: emptyList())
            }else {
                Resource.error("Failed to fetch moviesGenres: ${response.message()}", null)
            }
        }catch (e:Exception){
            Resource.error("An error occurred: ${e.message}", null)
        }
    }
    suspend fun getUpComing(page : Int): Resource<List<Movie>> {

        return try {
            val response = RetrofitClient.api.getUpComing(API_KEY,page)
            if (response.isSuccessful){

                Resource.success(response.body()?.results ?: emptyList())
            }else {
                Resource.error("Failed to fetch moviesGenres: ${response.message()}", null)
            }
        }catch (e:Exception){
            Resource.error("An error occurred: ${e.message}", null)
        }
    }
}