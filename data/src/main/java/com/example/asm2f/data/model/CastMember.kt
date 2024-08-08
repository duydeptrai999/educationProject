package com.example.asm2f.data.model

data class Cast(
    val cast_id: Int,
    val character: String,
    val name: String,
    val profile_path: String?
)

data class CastResponse(
    val id: Int,
    val cast: List<Cast>
)

