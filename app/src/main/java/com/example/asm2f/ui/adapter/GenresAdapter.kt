package com.example.asm2f.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asm2f.data.model.Genres
import com.example.asm2f.databinding.ItemGenresBinding

class GenresAdapter (private val onGenreClick: (String) -> Unit) : ListAdapter<Genres, GenresAdapter.GenresViewHolder>(GenresDiffCallback()) {

    class GenresDiffCallback : DiffUtil.ItemCallback<Genres>() {
        override fun areItemsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            // So sánh các mục bằng một thuộc tính duy nhất, ví dụ: id
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genres, newItem: Genres): Boolean {
            // So sánh nội dung của mục
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val binding = ItemGenresBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.bind(getItem(position),onGenreClick)
    }

    inner class GenresViewHolder(private val binding: ItemGenresBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genres, onGenreClick: (String) -> Unit) {
            binding.textViewGenreName.text = genre.name

            binding.root.setOnClickListener {
                onGenreClick(genre.id)
            }
        }
    }
}
