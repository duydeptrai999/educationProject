package com.example.asm2f.data.model

data class Genres(
    val id: String,
    val name: String,
)

data class GenresResults(
    val genres: List<Genres>
)