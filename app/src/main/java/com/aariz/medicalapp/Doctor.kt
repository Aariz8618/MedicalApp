package com.aariz.medicalapp

data class Doctor(
    val name: String,
    val specialty: String,
    val location: String,
    val rating: Float,
    val reviewCount: Int,
    val imageResId: Int,
    var isFavorite: Boolean = false
)