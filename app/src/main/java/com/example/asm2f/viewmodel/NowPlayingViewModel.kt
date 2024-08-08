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
class NowPlayingViewModel @Inject constructor(private val repository : APIRepository):ViewModel() {


    private var _moviesNowPlaying = MutableLiveData<Resource<List<Movie>>>()
    val movieNowPlaying : LiveData<Resource<List<Movie>>> = _moviesNowPlaying


    private var currentPage = 1
    private var isLoading = false
    private var isEndOfList = false
    init {
        getNowPlayingMovie()
    }
    fun getNowPlayingMovie(){
        if (isLoading || isEndOfList) return
        isLoading = true
        _moviesNowPlaying.postValue(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getNowPlaying(currentPage)
            if (response.status == Status.SUCCESS && response.data?.isNotEmpty() == true) {
                val updatedList: List<Movie> = (_moviesNowPlaying.value?.data ?: emptyList()) + (response.data ?: emptyList())
                _moviesNowPlaying.postValue(Resource.success(updatedList))
                currentPage++
            } else {
                isEndOfList = response.data?.isEmpty() == true
                _moviesNowPlaying.postValue(Resource.error(response.message ?: "Error loading data", _moviesNowPlaying.value?.data))
            }
            isLoading = false
        }
    }
//    fun loadMovie() {
//        val apiKey = "d5b97a6fad46348136d87b78895a0c06"
//        val page = 6
//
//        RetrofitClient.api.getNowPlaying(apiKey, page).enqueue(object : Callback<MovieResult> {
//            override fun onResponse(call: Call<MovieResult>, response: Response<MovieResult>) {
//                if (response.isSuccessful) {
//                    val movieResults = response.body()
//
//                    val movies = movieResults?.results
//                    // dùng để lấy data về
//                    // postvalue để thông báo cho bên nghe data sử lý
//                    _moviesNowPlaying.postValue(Resource.success(movies))
//                } else {
//                    Log.e("MainActivity", "Failed to get data from API.")
//                }
//            }
//
//            override fun onFailure(call: Call<MovieResult>, t: Throwable) {
//                Log.e("MainActivity", "Failed to load movies", t)
//            }
//        })
//    }

    fun getNowPlaying(): LiveData<Resource<List<Movie>>> {
        return movieNowPlaying
    }
    fun reloadMovies() {
        currentPage = 1
        isEndOfList = false
        getNowPlayingMovie()
    }
}