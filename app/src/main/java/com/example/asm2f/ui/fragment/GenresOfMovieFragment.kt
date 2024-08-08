package com.example.asm2f.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asm2f.databinding.FragmentGenresOfMovieBinding
import com.example.asm2f.ui.adapter.GenresOfMovieAdapter
import com.example.asm2f.viewmodel.GetGenresViewModel
import com.example.asm2f.data.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenresOfMovieFragment : BaseFragment<FragmentGenresOfMovieBinding>() {

    private val viewModel: GetGenresViewModel by viewModels()
    private lateinit var genresOfMovieAdapter: GenresOfMovieAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenresOfMovieBinding {
        return FragmentGenresOfMovieBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genreId = arguments?.getString("genreId") ?: return
        viewModel.getGenresOfMoive(genreId)

        genresOfMovieAdapter = GenresOfMovieAdapter { movieId ->
            val action = GenresOfMovieFragmentDirections
                .actionGenresOfMovieFragmentToMovieDetailFragment(movieId.toInt())
            findNavController().navigate(action)
        }

        binding.recyclerViewMovies.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = genresOfMovieAdapter
        }

        viewModel.getGenresOfMovie.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    genresOfMovieAdapter.submitList(resource.data)
                }
                Status.ERROR -> {
                    showErrorDialog("Error", "Failed to load movies. Please try again.")
                }
                Status.LOADING -> {

                }
            }
        })
    }
}
