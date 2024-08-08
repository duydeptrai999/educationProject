package com.example.asm2f.data.repository

import android.content.Context
import com.example.asm2f.data.database.AppDatabase
import com.example.asm2f.data.model.FavoriteMovies

class MovieRepository(context: Context) {

    private val db: AppDatabase = AppDatabase.getDatabase(context)

    fun getAllFavoriteMovies(): List<FavoriteMovies> {
        return db.favoriteMovieDao().getAll()
    }

    fun addFavoriteMovie(favoriteMovie: FavoriteMovies) {
        db.favoriteMovieDao().insert(favoriteMovie)
    }

    fun removeFavoriteMovie(movieId: String) {
        db.favoriteMovieDao().delete(movieId)
    }

    fun getFavoriteMovieById(movieId: String): FavoriteMovies? {
        return db.favoriteMovieDao().getByMovieId(movieId)
    }
}
