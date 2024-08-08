package com.example.asm2f.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asm2f.R
import com.example.asm2f.data.model.Movie
import com.example.asm2f.databinding.ItemMovieBinding
import com.example.asm2f.viewmodel.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PopularAdapterVertical(
    private val movies: List<Movie>,
    private var favoriteMovies: List<String>,
    private val itemClickListener: OnItemClickListener,
    private val favoriteClickListener: PopularAdapter.OnFavoriteClickListener
) : RecyclerView.Adapter<PopularAdapterVertical.PopularViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movieId: Int)
    }


    class PopularViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie,
                 favoriteMovies: List<String>,
                 clickListener: OnItemClickListener,
                 favoriteClickListener: PopularAdapter.OnFavoriteClickListener
        ) {
            binding.title.text = movie.title
            binding.releaseDate.text = movie.releaseDate
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(binding.poster)

            if (favoriteMovies.contains(movie.id)) {
                binding.favorite.setImageResource(R.drawable.baseline_favorit)
            } else {
                binding.favorite.setImageResource(R.drawable.baseline_favorite_border)
            }

            binding.root.setOnClickListener {
                clickListener.onItemClick(movie.id.toInt())
            }

            binding.favorite.setOnClickListener {
                favoriteClickListener.onFavoriteClick(movie.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(movies[position],favoriteMovies, itemClickListener,favoriteClickListener)
    }

    override fun getItemCount() = movies.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavoriteMovies(favoriteMovies: List<String>) {
        this.favoriteMovies = favoriteMovies
        notifyDataSetChanged()
    }
}
