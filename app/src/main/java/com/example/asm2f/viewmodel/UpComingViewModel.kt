package com.example.asm2f.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asm2f.data.Resource
import com.example.asm2f.data.Status
import com.example.asm2f.data.model.Movie
import com.example.asm2f.data.repository.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpComingViewModel @Inject constructor(private val repository : APIRepository):ViewModel() {


    private var _moviesUpComing = MutableLiveData<Resource<List<Movie>>>()
    val moviesUpComing: LiveData<Resource<List<Movie>>> = _moviesUpComing


    private var currentPage = 1
    private var isLoading = false
    private var isEndOfList = false
    init {
        getUpComingMovie()
    }
    fun getUpComingMovie(){

        if (isLoading || isEndOfList) return
        isLoading = true
        _moviesUpComing.postValue(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getUpComing(currentPage)
            if (response.status == Status.SUCCESS && response.data?.isNotEmpty() == true) {
                val updatedList: List<Movie> = (_moviesUpComing.value?.data ?: emptyList()) + (response.data ?: emptyList())
                _moviesUpComing.postValue(Resource.success(updatedList))
                currentPage++
            } else {
                isEndOfList = response.data?.isEmpty() == true
                _moviesUpComing.postValue(Resource.error(response.message ?: "Error loading data", _moviesUpComing.value?.data))
            }
            isLoading = false
        }
    }


    fun reloadMovies() {
        currentPage = 1
        isEndOfList = false
        getUpComingMovie()
    }
}