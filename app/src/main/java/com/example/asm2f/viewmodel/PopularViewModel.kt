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
    class  PopularViewModel @Inject constructor(private val repository : APIRepository): ViewModel(){


        private var _moviesPopular = MutableLiveData<Resource<List<Movie>>>()
        val moviesPopular: LiveData<Resource<List<Movie>>> = _moviesPopular

        private var currentPage = 1
        private var isLoading = false
        private var isEndOfList = false
        init {
            getPopularMovie()
        }

        fun getPopularMovie(){
//            _moviesPopular.postValue(Resource.loading(null))
//            viewModelScope.launch {
//                val response = repository.getPopularMovie(1)
//                _moviesPopular.postValue(response)
//            }
// dùng job để handle lại đoạn này
            if (isLoading || isEndOfList) return
            isLoading = true
            _moviesPopular.postValue(Resource.loading(null))
            viewModelScope.launch {
                val response = repository.getPopularMovie(currentPage)
                if (response.status == Status.SUCCESS && response.data?.isNotEmpty() == true) {
                    val updatedList: List<Movie> = (_moviesPopular.value?.data ?: emptyList()) + (response.data ?: emptyList())
                    _moviesPopular.postValue(Resource.success(updatedList))
                    currentPage++
                } else {
                    isEndOfList = response.data?.isEmpty() == true
                    _moviesPopular.postValue(Resource.error(response.message ?: "Error loading data", _moviesPopular.value?.data))
                }
                isLoading = false
            }
        }

        fun reloadMovies() {
            currentPage = 1
            isEndOfList = false
            getPopularMovie()
        }
    }