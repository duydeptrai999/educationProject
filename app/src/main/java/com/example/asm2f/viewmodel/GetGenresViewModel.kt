package com.example.asm2f.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asm2f.data.Resource
import com.example.asm2f.data.model.Genres
import com.example.asm2f.data.model.Movie
import com.example.asm2f.data.repository.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetGenresViewModel @Inject constructor(private val repository : APIRepository) :ViewModel() {

    private val _getGenres = MutableLiveData<Resource<List<Genres>>>()
    val getGenres: LiveData<Resource<List<Genres>>> = _getGenres
    private  val _getGenresOfMovie = MutableLiveData<Resource<List<Movie>>>()
    val getGenresOfMovie : LiveData<Resource<List<Movie>>> = _getGenresOfMovie
    init {
        getGenres()
    }
    fun getGenres() {
        _getGenres.postValue(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getGenres()
//            if (response.status == Status.SUCCESS) {
//                response.data?.let {
//                    for (genre in it) {
//                 //       Log.d("GetGenresViewModel", "Genre: ${genre.name}")
//                    }
//                }
//            } else {
//                Log.e("GetGenresViewModel", "Error: ${response.message}")
//            }
            _getGenres.postValue(response)
        }
    }
        fun getGenresOfMoive(genreId : String){
            _getGenresOfMovie.postValue(Resource.loading(null))
            viewModelScope.launch {
                val respon = repository.getGenresOfMovie(genreId)
                _getGenresOfMovie.postValue(respon)

            }
        }
//    fun getGenreIds(genres: List<Genres>): List<Int> {
//        return genres.map { it.id.toInt() }
//    }
}