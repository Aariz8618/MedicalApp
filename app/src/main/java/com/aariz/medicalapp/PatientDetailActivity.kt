package com.aariz.medicalapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PatientDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PATIENT_ID = "extra_patient_id"
        const val EXTRA_PATIENT_NAME = "extra_patient_name"
        const val EXTRA_PATIENT_AGE = "extra_patient_age"
        const val EXTRA_PATIENT_GENDER = "extra_patient_gender"
        const val EXTRA_PATIENT_CONDITION = "extra_patient_condition"
        const val EXTRA_PATIENT_STATUS = "extra_patient_status"
        const val EXTRA_PATIENT_LAST_VISIT = "extra_patient_last_visit"
        const val EXTRA_PATIENT_BLOOD_GROUP = "extra_patient_blood_group"
        const val EXTRA_PATIENT_ALLERGIES = "extra_patient_allergies_count"
        const val EXTRA_PATIENT_MEDICAL_HISTORY = "extra_patient_medical_history"
        const val EXTRA_PATIENT_MEDICATIONS = "extra_patient_medications"
        const val EXTRA_PATIENT_VISIT_NOTES = "extra_patient_visit_notes"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_detail)

        val name = intent.getStringExtra(EXTRA_PATIENT_NAME) ?: "Unknown Patient"
        val age = intent.getIntExtra(EXTRA_PATIENT_AGE, 0)
        val gender = intent.getStringExtra(EXTRA_PATIENT_GENDER) ?: ""
        val condition = intent.getStringExtra(EXTRA_PATIENT_CONDITION) ?: ""
        val lastVisit = intent.getStringExtra(EXTRA_PATIENT_LAST_VISIT) ?: ""
        val bloodGroup = intent.getStringExtra(EXTRA_PATIENT_BLOOD_GROUP) ?: "A+"
        val allergiesCount = intent.getIntExtra(EXTRA_PATIENT_ALLERGIES, 0)
        val medicalHistory = intent.getStringExtra(EXTRA_PATIENT_MEDICAL_HISTORY) ?: ""
        val medications = intent.getStringExtra(EXTRA_PATIENT_MEDICATIONS) ?: ""
        val visitNotes = intent.getStringExtra(EXTRA_PATIENT_VISIT_NOTES) ?: ""

        findViewById<TextView>(R.id.tvDetailName).text = name
        findViewById<TextView>(R.id.tvDetailDiagnosis).text = condition
        findViewById<TextView>(R.id.tvDetailAge).text = age.toString()
        findViewById<TextView>(R.id.tvDetailBlood).text = bloodGroup
        findViewById<TextView>(R.id.tvDetailAllergies).text = allergiesCount.toString()
        if (medicalHistory.isNotEmpty()) {
            findViewById<TextView>(R.id.tvDetailMedicalHistory).text = medicalHistory
        }
        if (medications.isNotEmpty()) {
            findViewById<TextView>(R.id.tvDetailMedications).text = medications
        }
        if (visitNotes.isNotEmpty()) {
            findViewById<TextView>(R.id.tvDetailVisitNotes).text = visitNotes
        }
        if (lastVisit.isNotEmpty()) {
            findViewById<TextView>(R.id.tvVisitNoteDate).text = lastVisit
        }

        findViewById<android.view.View>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
