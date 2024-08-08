package com.example.asm2f.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.asm2f.data.repository.APIRepository
import com.example.asm2f.data.repository.MovieRepository
import com.example.asm2f.data.retrofit.RetrofitClient
import com.example.asm2f.ui.MovieUIDetailCompose
import com.example.asm2f.viewmodel.MovieDetailViewModel
import com.example.asm2f.viewmodel.GetGenresViewModel
import com.example.asm2f.viewmodel.PopularViewModel
import com.example.asm2f.viewmodel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
// because this fragment use jetpackcompose so cant use basefragment(based on viewbinding) in this fragment
@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by viewModels()
    private val  getGenresViewModel: GetGenresViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val movieId = arguments?.getInt("movieId") ?: return null

        return ComposeView(requireContext()).apply {
            setContent {
                val movieUI = MovieUIDetailCompose ()
                movieUI.MovieDetailScreen(viewModel, movieId,getGenresViewModel)
            }
        }
    }

    companion object {
        fun newInstance(movieId: Int): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            val args = Bundle()
            args.putInt("movieId", movieId)
            fragment.arguments = args
            return fragment
        }
    }
}
