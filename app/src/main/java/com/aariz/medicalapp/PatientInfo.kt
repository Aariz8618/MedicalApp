package com.aariz.medicalapp

data class PatientInfo(
    val id: Int = 0,
    val name: String,
    val age: Int,
    val gender: String,
    val lastVisit: String,
    val condition: String,
    val status: String,
    val imageResId: Int = 0,
    val phone: String = "",
    val bloodGroup: String = "A+",
    val allergiesCount: Int = 0,
    val medicalHistory: String = "",
    val medications: String = "",
    val visitNotes: String = ""
)
