package com.example.asm2f.ui.fragment

import ViewPagerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asm2f.R
import com.example.asm2f.data.Status
import com.example.asm2f.data.model.Movie
import com.example.asm2f.data.repository.APIRepository
import com.example.asm2f.data.repository.MovieRepository
import com.example.asm2f.databinding.FragmentAllMoviesBinding
import com.example.asm2f.ui.adapter.*
import com.example.asm2f.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMoviesFragment : BaseFragment<FragmentAllMoviesBinding>(),
    PopularAdapter.OnItemClickListener,
    PopularAdapter.OnFavoriteClickListener,
    PopularAdapterVertical.OnItemClickListener,
    ToggleAdapterFragment,
    OnFragmentInteractionListener {

    private val allMovieViewModel: AllMovieViewModel by viewModels()
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var topRateAdapter: PopularAdapter
    private lateinit var upcomingAdapter: PopularAdapter
    private lateinit var nowPlayingAdapter: PopularAdapter
    private lateinit var popularAdapterVertical: PopularAdapterVertical
    private lateinit var topRateAdapterVertical: PopularAdapterVertical
    private lateinit var upcomingAdapterVertical: PopularAdapterVertical
    private lateinit var nowPlayingAdapterVertical: PopularAdapterVertical
    private val movieViewModel: MovieViewModel by viewModels()

    private val popularMovies = mutableListOf<Movie>()
    private val topRateMovies = mutableListOf<Movie>()
    private val upcomingMovies = mutableListOf<Movie>()
    private val nowPlayingMovies = mutableListOf<Movie>()
    private val favoriteMovies = mutableListOf<String>()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var listener: OnFragmentInteractionListener? = null

    private var isToggleAdapter = false

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAllMoviesBinding {
        return FragmentAllMoviesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = ViewPagerAdapter(requireActivity())

        setupRecyclerViews()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerViews() {
        binding.rvPopularMovies.apply {
            layoutManager = LinearLayoutManager(context)
            popularAdapter = PopularAdapter(
                popularMovies,
                favoriteMovies,
                this@AllMoviesFragment,
                this@AllMoviesFragment
            )
            adapter = popularAdapter
        }

        binding.rvTopRateMovies.apply {
            layoutManager = LinearLayoutManager(context)
            topRateAdapter = PopularAdapter(
                topRateMovies,
                favoriteMovies,
                this@AllMoviesFragment,
                this@AllMoviesFragment
            )
            adapter = topRateAdapter
        }

        binding.rvUpcomingMovies.apply {
            layoutManager = LinearLayoutManager(context)
            upcomingAdapter = PopularAdapter(
                upcomingMovies,
                favoriteMovies,
                this@AllMoviesFragment,
                this@AllMoviesFragment
            )
            adapter = upcomingAdapter
        }

        binding.rvNowPlayingMovies.apply {
            layoutManager = LinearLayoutManager(context)
            nowPlayingAdapter = PopularAdapter(
                nowPlayingMovies,
                favoriteMovies,
                this@AllMoviesFragment,
                this@AllMoviesFragment
            )
            adapter = nowPlayingAdapter
        }

        popularAdapterVertical = PopularAdapterVertical(
            popularMovies,
            favoriteMovies,
            this@AllMoviesFragment,
            this@AllMoviesFragment
        )
        topRateAdapterVertical = PopularAdapterVertical(
            topRateMovies,
            favoriteMovies,
            this@AllMoviesFragment,
            this@AllMoviesFragment
        )
        upcomingAdapterVertical = PopularAdapterVertical(
            upcomingMovies,
            favoriteMovies,
            this@AllMoviesFragment,
            this@AllMoviesFragment
        )
        nowPlayingAdapterVertical = PopularAdapterVertical(
            nowPlayingMovies,
            favoriteMovies,
            this@AllMoviesFragment,
            this@AllMoviesFragment
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObservers() {
        allMovieViewModel.tabs.observe(viewLifecycleOwner) { tabs ->
            val listSections = listOf(
                binding.popularMoviesSection,
                binding.topRateMoviesSection,
                binding.upComingMoviesSection,
                binding.nowPlayingMoviesSection
            )
            listSections.forEachIndexed { index, section ->
                if (!tabs.contains(index)) section.visibility = View.GONE
            }
        }
        allMovieViewModel.allMovieType.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { movieLists ->
                        val listsMovies = listOf(
                            popularMovies, topRateMovies, upcomingMovies, nowPlayingMovies
                        ).filterIndexed { index, movies ->
                            allMovieViewModel.tabs.value!!.contains(index)
                        }.forEachIndexed { index, movies ->
                            movies.clear()
                            movies.addAll(movieLists[index])
                        }

                        popularAdapter.notifyDataSetChanged()
                        topRateAdapter.notifyDataSetChanged()
                        upcomingAdapter.notifyDataSetChanged()
                        nowPlayingAdapter.notifyDataSetChanged()
                        popularAdapterVertical.notifyDataSetChanged()
                        topRateAdapterVertical.notifyDataSetChanged()
                        upcomingAdapterVertical.notifyDataSetChanged()
                        nowPlayingAdapterVertical.notifyDataSetChanged()
                    }

                    binding.swipeRefreshLayout6.isRefreshing = false
                    binding.proressShow6.visibility = View.INVISIBLE
                }

                Status.ERROR -> {
                    showErrorDialog("Lỗi", "Không thể tải dữ liệu, vui lòng thử lại sau.")
                    binding.swipeRefreshLayout6.isRefreshing = false
                }

                Status.LOADING -> {
                    binding.swipeRefreshLayout6.isRefreshing = true
                    binding.proressShow6.visibility = View.VISIBLE
                }
            }
        })

        binding.swipeRefreshLayout6.apply {
            allMovieViewModel.reloadMovies()
        }
        movieViewModel.favoriteMovies.observe(viewLifecycleOwner, Observer { favoriteList ->
            favoriteMovies.clear()
            favoriteMovies.addAll(favoriteList.map { it.movieId })
            popularAdapter.updateFavoriteMovies(favoriteMovies)
            popularAdapterVertical.updateFavoriteMovies(favoriteMovies)
            topRateAdapter.updateFavoriteMovies(favoriteMovies)
            upcomingAdapter.updateFavoriteMovies(favoriteMovies)
            nowPlayingAdapter.updateFavoriteMovies(favoriteMovies)
            topRateAdapterVertical.updateFavoriteMovies(favoriteMovies)
            upcomingAdapterVertical.updateFavoriteMovies(favoriteMovies)
            nowPlayingAdapterVertical.updateFavoriteMovies(favoriteMovies)
        })
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout6.setOnRefreshListener {
            allMovieViewModel.reloadMovies()
        }
        binding.popularViewAll.setOnClickListener {
            listener?.onPageRequested(1)
        }

        binding.topRateViewAll.setOnClickListener {
            listener?.onPageRequested(2)
        }

        binding.upComingViewAll.setOnClickListener {
            listener?.onPageRequested(3)
        }

        binding.nowPlayingViewAll.setOnClickListener {
            listener?.onPageRequested(4)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    private fun updateRecyclerView(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        adapterVertical: RecyclerView.Adapter<*>
    ) {
        if (isToggleAdapter) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = adapterVertical
        }
    }

    override fun onItemClick(movieId: Int) {
        val detailFragment = MovieDetailFragment.newInstance(movieId)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.nav_host_fragment, detailFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun toggleAdapter() {
        isToggleAdapter = !isToggleAdapter
        updateRecyclerView(binding.rvPopularMovies, popularAdapter, popularAdapterVertical)
        updateRecyclerView(binding.rvTopRateMovies, topRateAdapter, topRateAdapterVertical)
        updateRecyclerView(binding.rvUpcomingMovies, upcomingAdapter, upcomingAdapterVertical)
        updateRecyclerView(binding.rvNowPlayingMovies, nowPlayingAdapter, nowPlayingAdapterVertical)
    }

    override fun onFavoriteClick(movieId: String) {
        movieViewModel.isFavoriteMovie(movieId).observe(viewLifecycleOwner, Observer { isFavorite ->
            if (isFavorite) {
                movieViewModel.removeFavoriteMovie(movieId)
            } else {
                movieViewModel.addFavoriteMovie(movieId)
            }
        })
    }

    override fun onPageRequested(pageIndex: Int) {
        // Implementation for page navigation
    }
}
