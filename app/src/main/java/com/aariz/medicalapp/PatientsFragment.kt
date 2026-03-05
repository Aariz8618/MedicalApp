package com.aariz.medicalapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class PatientsFragment : Fragment(R.layout.my_patients) {

    private lateinit var adapter: PatientListAdapter
    private val allPatients = mutableListOf<PatientInfo>()
    private val filteredPatients = mutableListOf<PatientInfo>()
    private var currentFilter = "All"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<FrameLayout>(R.id.btnBack)
        btnBack.setOnClickListener {
            (activity as MainDoctorActivity)
                .loadFragment(DashboardFragment(), "back")
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                .selectedItemId = R.id.nav_dashboard
        }

        setupDummyData()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPatients)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PatientListAdapter(filteredPatients) { patient ->
            openPatientDetail(patient)
        }
        recyclerView.adapter = adapter

        updateFilteredList(view)

        val tabAll = view.findViewById<TextView>(R.id.tab_all)
        val tabActive = view.findViewById<TextView>(R.id.tab_active)
        val tabNew = view.findViewById<TextView>(R.id.tab_new)
        val tabDischarged = view.findViewById<TextView>(R.id.tab_discharged)
        val tabCritical = view.findViewById<TextView>(R.id.tab_critical)
        val tabs = listOf(tabAll, tabActive, tabNew, tabDischarged, tabCritical)
        val filterMap = mapOf(
            tabAll to "All",
            tabActive to "Active",
            tabNew to "New",
            tabDischarged to "Discharged",
            tabCritical to "Critical"
        )

        tabs.forEach { tab ->
            tab.setOnClickListener {
                tabs.forEach { t ->
                    t.setBackgroundResource(R.drawable.tab_unselected_bg)
                    t.setTextColor(resources.getColor(R.color.primary_dark, null))
                }
                tab.setBackgroundResource(R.drawable.tab_selected_bg)
                tab.setTextColor(resources.getColor(android.R.color.white, null))
                currentFilter = filterMap[tab] ?: "All"
                updateFilteredList(view)
            }
        }

        view.findViewById<EditText>(R.id.searchEditText)
            .addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filterBySearch(view, s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
    }

    private fun openPatientDetail(patient: PatientInfo) {
        val intent = Intent(requireContext(), PatientDetailActivity::class.java).apply {
            putExtra(PatientDetailActivity.EXTRA_PATIENT_ID, patient.id)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_NAME, patient.name)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_AGE, patient.age)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_GENDER, patient.gender)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_CONDITION, patient.condition)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_STATUS, patient.status)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_LAST_VISIT, patient.lastVisit)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_BLOOD_GROUP, patient.bloodGroup)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_ALLERGIES, patient.allergiesCount)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_MEDICAL_HISTORY, patient.medicalHistory)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_MEDICATIONS, patient.medications)
            putExtra(PatientDetailActivity.EXTRA_PATIENT_VISIT_NOTES, patient.visitNotes)
        }
        startActivity(intent)
    }

    private fun setupDummyData() {
        allPatients.addAll(
            listOf(
                PatientInfo(
                    id = 1,
                    name = "Emily Anderson",
                    age = 34,
                    gender = "Female",
                    lastVisit = "Feb 20, 2026",
                    condition = "Hypertension",
                    status = "Active",
                    bloodGroup = "A+",
                    allergiesCount = 1,
                    medicalHistory = "Hypertension diagnosed in 2018. Managed with lifestyle changes and medication. No prior hospitalizations.",
                    medications = "Amlodipine 5mg – Once daily\nLisinopril 10mg – Once daily",
                    visitNotes = "BP stable at 130/85. Patient reports mild headaches. Advised to monitor BP twice daily and reduce sodium intake."
                ),
                PatientInfo(
                    id = 2,
                    name = "James Carter",
                    age = 45,
                    gender = "Male",
                    lastVisit = "Feb 18, 2026",
                    condition = "Diabetes Type 2",
                    status = "Active",
                    bloodGroup = "O+",
                    allergiesCount = 0,
                    medicalHistory = "Type 2 Diabetes diagnosed in 2020. Well-controlled with diet and metformin.",
                    medications = "Metformin 500mg – Twice daily\nAtorvastatin 10mg – Once at night",
                    visitNotes = "HbA1c 6.8%. Advised to continue current regimen and increase physical activity."
                ),
                PatientInfo(
                    id = 3,
                    name = "Sophia Williams",
                    age = 28,
                    gender = "Female",
                    lastVisit = "Feb 15, 2026",
                    condition = "Acute Hepatitis",
                    status = "Critical",
                    bloodGroup = "B+",
                    allergiesCount = 2,
                    medicalHistory = "Acute Hepatitis B. Elevated liver enzymes. Requires close monitoring.",
                    medications = "Tenofovir 300mg – Once daily\nVitamin B complex – Once daily",
                    visitNotes = "Liver enzymes elevated (ALT 280). Advised bed rest and adequate hydration. Follow-up in 1 week."
                ),
                PatientInfo(
                    id = 4,
                    name = "Michael Brown",
                    age = 52,
                    gender = "Male",
                    lastVisit = "Feb 12, 2026",
                    condition = "Cardiomegaly",
                    status = "Active",
                    bloodGroup = "AB+",
                    allergiesCount = 1,
                    medicalHistory = "Cardiomegaly with mild left ventricular dysfunction. On cardiac medications since 2021.",
                    medications = "Carvedilol 6.25mg – Twice daily\nFurosemide 20mg – Once daily",
                    visitNotes = "Echo shows stable LV function. BNP slightly elevated. Continue current medications."
                ),
                PatientInfo(
                    id = 5,
                    name = "Priya Sharma",
                    age = 30,
                    gender = "Female",
                    lastVisit = "Feb 10, 2026",
                    condition = "Hypothyroidism",
                    status = "New",
                    bloodGroup = "A-",
                    allergiesCount = 0,
                    medicalHistory = "Newly diagnosed hypothyroidism. TSH elevated at 8.2. No prior thyroid issues.",
                    medications = "Levothyroxine 50mcg – Once daily (morning, empty stomach)",
                    visitNotes = "TSH 8.2, Free T4 low. Started on Levothyroxine. Repeat TFT in 6 weeks."
                ),
                PatientInfo(
                    id = 6,
                    name = "Ravi Kumar",
                    age = 60,
                    gender = "Male",
                    lastVisit = "Feb 8, 2026",
                    condition = "Arrhythmia",
                    status = "Active",
                    bloodGroup = "O-",
                    allergiesCount = 3,
                    medicalHistory = "Atrial fibrillation since 2019. Anticoagulation therapy ongoing. Pacemaker evaluation pending.",
                    medications = "Warfarin 5mg – Once daily\nBisoprolol 5mg – Once daily\nApixaban 5mg – Twice daily",
                    visitNotes = "INR 2.4 (within range). Heart rate 72 bpm at rest. Continue current anticoagulation."
                ),
                PatientInfo(
                    id = 7,
                    name = "Ananya Patel",
                    age = 22,
                    gender = "Female",
                    lastVisit = "Jan 28, 2026",
                    condition = "Anemia",
                    status = "Discharged",
                    bloodGroup = "B-",
                    allergiesCount = 0,
                    medicalHistory = "Iron-deficiency anemia. Treated with IV iron infusion. Discharged with oral supplements.",
                    medications = "Ferrous sulfate 200mg – Once daily",
                    visitNotes = "Hb improved to 11.2 g/dL post-infusion. Discharged. Follow-up in 4 weeks."
                ),
                PatientInfo(
                    id = 8,
                    name = "David Lee",
                    age = 38,
                    gender = "Male",
                    lastVisit = "Jan 20, 2026",
                    condition = "Pneumonia",
                    status = "Discharged",
                    bloodGroup = "A+",
                    allergiesCount = 1,
                    medicalHistory = "Community-acquired pneumonia. Treated with IV antibiotics for 5 days. Full recovery.",
                    medications = "Amoxicillin 500mg – Three times daily (completed course)",
                    visitNotes = "Chest X-ray clear. SpO2 98% on room air. Discharged with oral antibiotics to complete the course."
                )
            )
        )
    }

    private fun updateFilteredList(view: View) {
        filteredPatients.clear()
        filteredPatients.addAll(
            if (currentFilter == "All") allPatients
            else allPatients.filter { it.status == currentFilter }
        )
        view.findViewById<TextView>(R.id.tvResultCount)?.text = "${filteredPatients.size} patients"
        adapter.notifyDataSetChanged()
    }

    private fun filterBySearch(view: View, query: String) {
        filteredPatients.clear()
        val base = if (currentFilter == "All") allPatients else allPatients.filter { it.status == currentFilter }
        filteredPatients.addAll(
            if (query.isEmpty()) base
            else base.filter { it.name.contains(query, ignoreCase = true) }
        )
        view.findViewById<TextView>(R.id.tvResultCount)?.text = "${filteredPatients.size} patients"
        adapter.notifyDataSetChanged()
    }
}

