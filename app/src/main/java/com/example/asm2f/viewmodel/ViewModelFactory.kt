package com.example.asm2f.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.asm2f.data.repository.APIRepository
import com.example.asm2f.data.repository.MovieRepository
import com.example.asm2f.data.retrofit.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel

class ViewModelFactory(
    private val repository: MovieRepository,
    private val retrofitClient: APIRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PopularViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                PopularViewModel(retrofitClient) as T
            }
            modelClass.isAssignableFrom(NowPlayingViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                NowPlayingViewModel(retrofitClient) as T
            }
            modelClass.isAssignableFrom(TopRateViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                TopRateViewModel(retrofitClient) as T
            }
            modelClass.isAssignableFrom(UpComingViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                UpComingViewModel(retrofitClient) as T
            }
            modelClass.isAssignableFrom(AllMovieViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                AllMovieViewModel(retrofitClient) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SearchViewModel(retrofitClient) as T
            }

            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                MovieViewModel(repository) as T
            }
            modelClass.isAssignableFrom(GetGenresViewModel::class.java)-> {
                @Suppress("UNCHECKED_CAST")
                return GetGenresViewModel(retrofitClient) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
