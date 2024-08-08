package com.example.asm2f.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey @SerializedName("id") @Expose val id: String,
    @SerializedName("title") @Expose val title: String,
    @SerializedName("vote_average") @Expose val voteAverage: Float,
    @SerializedName("overview") @Expose val overview: String,
    @SerializedName("poster_path") @Expose val posterPath: String,
    @SerializedName("release_date") @Expose val releaseDate: String,
    @SerializedName("genre_ids") @Expose val genreIds: List<Int>,
    val genres : List<Genres>


)

@Entity(tableName = "FavoriteMovies")
data class FavoriteMovies(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") @Expose val id: Int,
    @SerializedName("movie_id") @Expose val movieId: String,
)
