package com.aariz.medicalapp

data class PatientInfo(
    val name: String,
    val age: Int,
    val gender: String,
    val lastVisit: String,
    val condition: String,
    val status: String,
    val imageResId: Int = 0,
    val phone: String = ""
)
