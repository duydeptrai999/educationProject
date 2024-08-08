package com.example.asm2f.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.example.asm2f.data.model.Movie
import com.example.asm2f.databinding.FragmentTopRateBinding
import com.example.asm2f.ui.adapter.PopularAdapter
import com.example.asm2f.ui.adapter.PopularAdapterVertical
import com.example.asm2f.viewmodel.MovieViewModel
import com.example.asm2f.data.Status
import com.example.asm2f.viewmodel.TopRateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopRateFragment : BaseFragment<FragmentTopRateBinding>(),
    PopularAdapter.OnItemClickListener,
    PopularAdapter.OnFavoriteClickListener,
    PopularAdapterVertical.OnItemClickListener,
    ToggleAdapterFragment {

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var popularAdapterVertical: PopularAdapterVertical
    private val movieViewModel: MovieViewModel by viewModels()
    private val topRateViewModel: TopRateViewModel by viewModels()
    private val favoriteMovies = mutableListOf<String>()
    private val movies = mutableListOf<Movie>()
    private var isToggleAdapter = false
    private var isLoadingMore = false

    private var currentScrollPosition = 0

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTopRateBinding {
        return FragmentTopRateBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popularAdapter = PopularAdapter(movies, favoriteMovies, this, this)
        popularAdapterVertical = PopularAdapterVertical(movies, favoriteMovies, this, this)

        binding.recycleView2.layoutManager = LinearLayoutManager(context)
        binding.recycleView2.adapter = popularAdapter

        topRateViewModel.moviesTopRate.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { movieList ->
                        updateMovieList(movieList)
                    }
                    binding.swipeRefreshLayout2.isRefreshing = false // Tắt indicator khi load xong
                    binding.proressShow2.visibility = View.INVISIBLE
                    isLoadingMore = false
                }

                Status.ERROR -> {
                    showErrorDialog("Lỗi", "Không thể tải dữ liệu, vui lòng thử lại sau.")
                    binding.swipeRefreshLayout2.isRefreshing = false
                    isLoadingMore = false
                }

                Status.LOADING -> {
                    binding.swipeRefreshLayout2.isRefreshing = true
                    binding.proressShow2.visibility = View.VISIBLE
                }
            }
        })

        movieViewModel.favoriteMovies.observe(viewLifecycleOwner, Observer { favoriteList ->
            favoriteMovies.clear()
            favoriteMovies.addAll(favoriteList.map { it.movieId })
            popularAdapter.updateFavoriteMovies(favoriteMovies)
        })

        binding.swipeRefreshLayout2.setOnRefreshListener {
            topRateViewModel.reloadMovies()
            binding.recycleView2.apply {
                movies.clear()
                scrollToPosition(movies.size)
            }
        }

        binding.recycleView2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoadingMore) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                        firstVisibleItemPosition >= 0
                    ) {
                        isLoadingMore = true
                        topRateViewModel.getTopRate()
                    }
                }
            }
        })
    }

    private fun updateMovieList(newMovies: List<Movie>) {
        val startIndex = movies.size
        movies.addAll(newMovies)
        popularAdapter.notifyItemRangeInserted(startIndex, newMovies.size)
        binding.recycleView2.scrollToPosition(startIndex - 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter() {
        currentScrollPosition =
            (binding.recycleView2.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        Log.d("PopularFragment", "Current Scroll Position: $currentScrollPosition")

        if (isToggleAdapter) {
            binding.recycleView2.layoutManager = GridLayoutManager(context, 2)
            binding.recycleView2.adapter = popularAdapterVertical
        } else {
            binding.recycleView2.layoutManager = LinearLayoutManager(context)
            binding.recycleView2.adapter = popularAdapter
        }
        binding.recycleView2.layoutManager?.scrollToPosition(currentScrollPosition)
        isToggleAdapter = !isToggleAdapter
    }

    override fun onItemClick(movieId: Int) {
        val detailFragment = MovieDetailFragment.newInstance(movieId)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.nav_host_fragment, detailFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun toggleAdapter() {
        updateAdapter()
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

    override fun showErrorDialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
