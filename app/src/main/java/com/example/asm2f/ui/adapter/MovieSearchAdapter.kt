package com.example.asm2f.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asm2f.data.model.Movie
import com.example.asm2f.databinding.ItemMovie1Binding

class MovieSearchAdapter  (private val onSearchClick: (String) -> Unit) : ListAdapter<Movie, MovieSearchAdapter.MovieViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovie1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie,onSearchClick)
    }

    class MovieViewHolder(private val binding: ItemMovie1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, onSearchClick: (String) -> Unit) {
            binding.title.text = movie.title
            binding.overview.text = movie.overview
            binding.releaseDate.text = movie.releaseDate
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(binding.poster)

            binding.root.setOnClickListener {
                onSearchClick(movie.id)
            }

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
