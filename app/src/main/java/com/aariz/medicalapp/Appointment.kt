package com.aariz.medicalapp

data class Appointment(
    val patientName: String,
    val type: String,
    val dateTime: String,
    val status: String
)