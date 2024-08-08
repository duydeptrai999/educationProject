package com.example.asm2f.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asm2f.data.Resource
import com.example.asm2f.data.Status
import com.example.asm2f.data.model.Movie
import com.example.asm2f.data.repository.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class AllMovieViewModel @Inject constructor(private val repository: APIRepository) : ViewModel() {
    private val _allMovieType = MutableLiveData<Resource<List<List<Movie>>>>()
    val allMovieType: LiveData<Resource<List<List<Movie>>>> = _allMovieType
    private val _tabs = MutableLiveData<List<Int>>()
    val tabs: LiveData<List<Int>> = _tabs

    init {
        getAllMovie()
    }

    fun getAllMovie() {
        _allMovieType.postValue(Resource.loading(null))
        viewModelScope.launch {

            val popularMoviesDeferred = async { getPopularViewModel() }
            val topRateMoviesDeferred = async { getTopRateViewModel() }
            val upComingMoviesDeferred = async { getUpComingViewModel() }
            val nowPlayingMoviesDeferred = async { getNowPlayingViewModel() }

            val response = listOf(
                popularMoviesDeferred.await(),
                topRateMoviesDeferred.await(),
                upComingMoviesDeferred.await(),
                nowPlayingMoviesDeferred.await()
            )

            if (response.all { it.status == Status.ERROR }) {
                _allMovieType.postValue(
                    Resource.error("fail api request", emptyList())
                )
            } else {
                val successRepose = mutableListOf<Int>()
                response.forEachIndexed { index, resource ->
                    if (resource.status == Status.SUCCESS)
                        successRepose.add(index)
                }
                val successResponses =
                    response.filterIndexed { index, _ -> successRepose.contains(index) }

                val allMovies = successResponses.map { it.data?.take(4) ?: emptyList() }
                _tabs.postValue(successRepose)
                _allMovieType.postValue(Resource.success(allMovies))
            }


        }
    }

    fun reloadMovies() {
        getAllMovie()
    }

    private suspend fun getPopularViewModel(): Resource<List<Movie>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = repository.getPopularMovie(1)
                result
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString(), null)
        }
    }

    private suspend fun getTopRateViewModel(): Resource<List<Movie>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = repository.getTopRate(1)
                result
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString(), null)
        }
    }

    private suspend fun getUpComingViewModel(): Resource<List<Movie>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = repository.getUpComing(1)
                result
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString(), null)
        }
    }

    private suspend fun getNowPlayingViewModel(): Resource<List<Movie>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = repository.getNowPlaying(1)
                result
            }
        } catch (e: Exception) {
            Resource.error(e.message.toString(), null)
        }
    }
}