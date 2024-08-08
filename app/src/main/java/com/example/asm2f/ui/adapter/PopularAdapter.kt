package com.example.asm2f.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asm2f.R
import com.example.asm2f.data.model.Movie
import com.example.asm2f.databinding.ItemMovie1Binding

class PopularAdapter(
    private val movies: List<Movie>,
    private var favoriteMovies: List<String>,
    private val itemClickListener: OnItemClickListener,
    private val favoriteClickListener: OnFavoriteClickListener
) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movieId: Int)
    }

    interface OnFavoriteClickListener {
        fun onFavoriteClick(movieId: String)
    }

    inner class PopularViewHolder(private val binding: ItemMovie1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            movie: Movie,
            favoriteMovies: List<String>,
            clickListener: OnItemClickListener,
            favoriteClickListener: OnFavoriteClickListener
        ) {
            binding.title.text = movie.title
            binding.overview.text = movie.overview
            binding.releaseDate.text = movie.releaseDate
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(binding.poster)

            Log.d("showList", "Movie ID: ${movie.id}, is favorite: $favoriteMovies")
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
        val binding = ItemMovie1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(movies[position], favoriteMovies, itemClickListener, favoriteClickListener)
    }

    override fun getItemCount() = movies.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavoriteMovies(favoriteMovies: List<String>) {
        this.favoriteMovies = favoriteMovies
        notifyDataSetChanged()
    }
}
