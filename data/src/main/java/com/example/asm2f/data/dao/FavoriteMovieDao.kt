package com.example.asm2f.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.asm2f.data.model.FavoriteMovies

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM FavoriteMovies WHERE movieId LIKE :movieId")
    fun getByMovieId(movieId: String): FavoriteMovies?

    @Query("SELECT * FROM FavoriteMovies")
    fun getAll(): List<FavoriteMovies>

    @Insert
    fun insert(favoriteMovie: FavoriteMovies)

    @Query("DELETE FROM FavoriteMovies WHERE movieId = :movieId")
    fun delete(movieId: String)
}
