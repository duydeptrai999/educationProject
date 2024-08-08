    package com.example.asm2f.viewmodel

    import android.util.Log
    import androidx.compose.runtime.MutableState
    import androidx.compose.runtime.mutableStateOf
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import com.example.asm2f.data.model.Cast
    import com.example.asm2f.data.model.CastResponse
    import com.example.asm2f.data.model.Movie
    import com.example.asm2f.data.retrofit.RetrofitClient
    import dagger.hilt.android.lifecycle.HiltViewModel
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response
    import javax.inject.Inject

    @HiltViewModel
    class MovieDetailViewModel @Inject constructor() : ViewModel() {

        val expanded: MutableState<Boolean> = mutableStateOf(false)
        private val _movieDetail = MutableLiveData<Movie>()
        val movieDetail: LiveData<Movie> get() = _movieDetail

        private val _cast = MutableLiveData<List<Cast>>()
        val cast: LiveData<List<Cast>> get() = _cast

        private val apiKey = "d5b97a6fad46348136d87b78895a0c06"

       fun fetchMovieDetail(movieId: Int) {
            RetrofitClient.api.getMovieDetail(movieId, apiKey).enqueue(object : Callback<Movie> {
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    if (response.isSuccessful) {
                        _movieDetail.postValue(response.body())
                        Log.d("MovieDetailViewModel","${response.body()}")
                         }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                       }
            })
        }

        fun fetchMovieCredits(movieId: Int) {
            RetrofitClient.api.getMovieCredits(movieId, apiKey).enqueue(object : Callback<CastResponse> {
                override fun onResponse(call: Call<CastResponse>, response: Response<CastResponse>) {
                    if (response.isSuccessful) {
                        _cast.postValue(response.body()?.cast)
                          }
                }

                override fun onFailure(call: Call<CastResponse>, t: Throwable) {

                }
            })
        }
    }
