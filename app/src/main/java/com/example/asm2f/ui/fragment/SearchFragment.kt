package com.example.asm2f.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asm2f.R
import com.example.asm2f.databinding.FragmentSearchBinding
import com.example.asm2f.ui.adapter.MovieSearchAdapter
import com.example.asm2f.viewmodel.SearchViewModel
import com.example.asm2f.data.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var movieSearchAdapter: MovieSearchAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private var hasUserStartedTyping = false

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieSearchAdapter = MovieSearchAdapter { movieId ->
            val action = SearchFragmentDirections
                .actionSearchFragmentToMovieDetailFragment(movieId.toInt())
            findNavController().navigate(action)
        }

        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.moviesRecyclerView.adapter = movieSearchAdapter

        binding.searchButton.setOnClickListener {
            try {
                val query = binding.searchEditText.text.toString()
                viewModel.searchMovies(query)
            } catch (e: Throwable) {
                Log.d("SearchFragment", "error ${e.message}")
            }
        }


        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!hasUserStartedTyping) {
                    hasUserStartedTyping = true

                }


                searchRunnable?.let { handler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    val query = s.toString()
                    viewModel.searchMovies(query)
                }
                handler.postDelayed(searchRunnable!!, 500)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.searchEditText.apply {
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = v.text.toString()
                    viewModel.searchMovies(query)
                    true
                } else {
                    false
                }
            }
        }

        viewModel.searchMovies.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { movieList ->
                        if (movieList.isEmpty()) {
                            movieSearchAdapter.submitList(emptyList())
                            binding.searchNormation.visibility = View.VISIBLE
                        } else {
                            movieSearchAdapter.submitList(movieList)
                            binding.searchNormation.visibility = View.GONE
                        }
                    }
                }
                Status.ERROR -> {
                    showErrorDialog("Error", "Failed to load search results. Please try again.")
                    Log.d("SearchFragment", "Error: ${resource.message}")
                    movieSearchAdapter.submitList(emptyList())
                    binding.searchNormation.visibility = View.VISIBLE
                }

                Status.LOADING -> {

                }
            }
        })
    }


    override fun showErrorDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("No Results")
            .setMessage("No movies found. Please try a different search query.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
