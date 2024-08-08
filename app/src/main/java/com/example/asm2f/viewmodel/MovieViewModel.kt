package com.example.asm2f.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asm2f.data.model.FavoriteMovies
import com.example.asm2f.data.model.Movie
import com.example.asm2f.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _favoriteMovies = MutableLiveData<List<FavoriteMovies>>()
    val favoriteMovies: LiveData<List<FavoriteMovies>> get() = _favoriteMovies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        loadFavoriteMovies()
    }

    fun loadFavoriteMovies() {

        _loading.postValue(true)


        CoroutineScope(Dispatchers.IO).launch  {
            try {
                val movies = withContext(Dispatchers.IO) {
                    repository.getAllFavoriteMovies()

                }   
                _favoriteMovies.postValue(movies)
                _loading.postValue(false)
            } catch (e: Exception) {
                _loading.postValue(false)
            }
        }
    }

    fun addFavoriteMovie(movieId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.addFavoriteMovie(FavoriteMovies(0, movieId))
                loadFavoriteMovies()
            } catch (e: Exception) {
                Log.d("duy", "addFavoriteMovie: ${e.message}")
                _error.postValue("Failed to add favorite movie: ${e.message}")
            }
        }
    }

    fun removeFavoriteMovie(movieId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.removeFavoriteMovie(movieId)
                loadFavoriteMovies()
            } catch (e: Exception) {

                Log.d("duy", "removeFavoriteMovie: ${e.message}")
                _error.postValue("Failed to remove favorite movie: ${e.message}")
            }
        }
    }

    fun isFavoriteMovie(movieId: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {

            val isFavorite = withContext(Dispatchers.IO) {
                repository.getFavoriteMovieById(movieId) != null
            }

            Log.d("MovieViewModel", "isFavoriteMovie result for movieId: $movieId is $isFavorite")

            result.postValue(isFavorite)
            }catch (e:Exception){
                Log.d("MovieViewModel", "Error in isFavoriteMovie: ${e.message}")

            }
        }
        return result
    }
}
