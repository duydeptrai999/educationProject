import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PopularViewModelTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @MockK
//    lateinit var retrofitClient: RetrofitClient
//
//    @MockK
//    lateinit var apiService: ApiRetrofitService
//
//    @MockK
//    lateinit var apiCall: Call<MovieResult>
//
//    private lateinit var popularViewModel: PopularViewModel
//
//    @Before
//    fun setUp() {
//        MockKAnnotations.init(this, relaxed = true)
//        mockkObject(RetrofitClient)
//        every { RetrofitClient.api } returns apiService
//        popularViewModel = PopularViewModel()
//    }
//
//    @Test
//    fun testSuccessGetPopularMovies() = runBlockingTest {
//        val movieList = listOf(Movie("1", "Title", 8.0f, "Over View", "Poster Path", "Release Date"))
//        val movieResult = MovieResult(page = 1, results = movieList)
//        val observer: Observer<Resource<List<Movie>>> = mockk(relaxed = true)
//
//        every { apiService.getPopular(any(), any()) } returns apiCall
//        every { apiCall.enqueue(any()) } answers {
//            val callback = it.invocation.args[0] as Callback<MovieResult>
//            callback.onResponse(apiCall, Response.success(movieResult))
//        }
//
//        popularViewModel.getPopularMovies().observeForever(observer)
//        popularViewModel.loadMovie()
//
//        verify { observer.onChanged(Resource.success(movieList)) }
//    }
}
