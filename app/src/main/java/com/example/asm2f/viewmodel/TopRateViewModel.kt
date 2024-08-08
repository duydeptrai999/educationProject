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
class TopRateViewModel @Inject constructor(private val repository : APIRepository):ViewModel() {


    private var _moviesTopRate = MutableLiveData<Resource<List<Movie>>>()
    val moviesTopRate :LiveData<Resource<List<Movie>>> = _moviesTopRate


    private var currentPage = 1
    private var isLoading = false
    private var isEndOfList = false
      init {
      getTopRate()
    }
    fun getTopRate(){


        if (isLoading || isEndOfList) return
        isLoading = true
        _moviesTopRate.postValue(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getTopRate(currentPage)
            if (response.status == Status.SUCCESS && response.data?.isNotEmpty() == true) {
                val updatedList: List<Movie> = (_moviesTopRate.value?.data ?: emptyList()) + (response.data ?: emptyList())
                _moviesTopRate.postValue(Resource.success(updatedList))
                currentPage++
            } else {
                isEndOfList = response.data?.isEmpty() == true
                _moviesTopRate.postValue(Resource.error(response.message ?: "Error loading data", _moviesTopRate.value?.data))
            }
            isLoading = false
        }
    }

    fun getTopRateMovies(): LiveData<Resource<List<Movie>>> {
        return _moviesTopRate
    }
    fun reloadMovies() {
        currentPage = 1
        isEndOfList = false
        getTopRate()
    }
}