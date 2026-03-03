package com.aariz.medicalapp

data class Report(
    val id: String = "",
    val patientName: String = "",
    val patientImage: Int = 0,
    val reportType: String = "",
    val reportCategory: String = "",
    val date: String = "",
    val status: String = "",
    val summary: String = "",
    val extractedText: String = "",
    val entities: List<MedicalEntity> = emptyList(),
    val abnormalValues: List<AbnormalValue> = emptyList(),
    val doctorNotes: String = ""
)

data class MedicalEntity(
    val type: String = "",
    val name: String = "",
    val value: String = "",
    val icon: Int = 0
)

data class AbnormalValue(
    val name: String = "",
    val value: String = "",
    val normalRange: String = "",
    val isHigh: Boolean = true
)
