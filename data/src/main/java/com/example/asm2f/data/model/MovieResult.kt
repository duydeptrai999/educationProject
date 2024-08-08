package com.example.asm2f.data.model

import androidx.room.Entity
import com.example.asm2f.data.model.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class MovieResult(
    @SerializedName("page") @Expose val page: Int,
    @SerializedName("results") @Expose val results: List<Movie>,
)