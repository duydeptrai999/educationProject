package com.example.asm2f.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asm2f.data.Resource
import com.example.asm2f.data.model.Movie
import com.example.asm2f.data.repository.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: APIRepository) : ViewModel() {

    private val _searchMovies = MutableLiveData<Resource<List<Movie>>>()
    val searchMovies: LiveData<Resource<List<Movie>>> = _searchMovies

    fun searchMovies(queryString: String) {
        _searchMovies.postValue(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchMovie(queryString)
            _searchMovies.postValue(response)
        }
    }
}
