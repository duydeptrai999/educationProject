package com.example.asm2f

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.asm2f.data.model.Cast
import com.example.asm2f.data.model.CastResponse
import com.example.asm2f.data.model.Movie
import com.example.asm2f.data.retrofit.ApiRetrofitService
import com.example.asm2f.data.retrofit.RetrofitClient
import com.example.asm2f.viewmodel.MovieDetailViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var retrofitClient: RetrofitClient

    @MockK
    lateinit var apiService: ApiRetrofitService

    @MockK
    lateinit var movieCall: Call<Movie>

    @MockK
    lateinit var castCall: Call<CastResponse>

    private lateinit var movieDetailViewModel: MovieDetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        mockkObject(RetrofitClient)
        every { RetrofitClient.api } returns apiService
        movieDetailViewModel = MovieDetailViewModel()
    }

    @Test
    fun testFetchMovieDetailSuccess() = runBlockingTest {
        val movie = Movie("1", "Title", 8.0f, "Over View", "Poster Path", "Release Date")
        val observer: Observer<Movie> = mockk(relaxed = true)

        every { apiService.getMovieDetail(any(), any()) } returns movieCall
        every { movieCall.enqueue(any()) } answers {
            val callback = it.invocation.args[0] as Callback<Movie>
            callback.onResponse(movieCall, Response.success(movie))
        }

        movieDetailViewModel.movieDetail.observeForever(observer)
        movieDetailViewModel.fetchMovieDetail(1)

        verify { observer.onChanged(movie) }
    }


    @Test
    fun testFetchMovieCreditsSuccess() = runBlockingTest {
        val castList = listOf(Cast(1, "Name", "Character", "Profile Path"))
        val castResponse = CastResponse(1,castList)
        val observer: Observer<List<Cast>> = mockk(relaxed = true)

        every { apiService.getMovieCredits(any(), any()) } returns castCall
        every { castCall.enqueue(any()) } answers {
            val callback = it.invocation.args[0] as Callback<CastResponse>
            callback.onResponse(castCall, Response.success(castResponse))
        }

        movieDetailViewModel.cast.observeForever(observer)
        movieDetailViewModel.fetchMovieCredits(1)

        verify { observer.onChanged(castList) }
    }
}

