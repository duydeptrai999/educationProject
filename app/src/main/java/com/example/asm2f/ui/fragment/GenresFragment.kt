package com.example.asm2f.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asm2f.databinding.FragmentGenresBinding
import com.example.asm2f.ui.adapter.GenresAdapter
import com.example.asm2f.viewmodel.GetGenresViewModel
import com.example.asm2f.data.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenresFragment : BaseFragment<FragmentGenresBinding>() {

    private val viewModel: GetGenresViewModel by viewModels()
    private lateinit var genresAdapter: GenresAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGenresBinding {
        return FragmentGenresBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        genresAdapter = GenresAdapter { genreId ->
            val action = GenresFragmentDirections
                .actionGenresFragmentToGenresOfMovieFragment(genreId)
            findNavController().navigate(action)
        }

        binding.recyclerViewGenres.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = genresAdapter
        }

        viewModel.getGenres()

        viewModel.getGenres.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    genresAdapter.submitList(resource.data)
                }
                Status.ERROR -> {
                    showErrorDialog("Error", "Failed to load genres. Please try again.")
                }
                Status.LOADING -> {
                    // Handle loading state if needed
                }
            }
        })
    }
}
