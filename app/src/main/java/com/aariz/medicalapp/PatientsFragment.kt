package com.aariz.medicalapp

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
        adapter = PatientListAdapter(filteredPatients)
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

    private fun setupDummyData() {
        allPatients.addAll(
            listOf(
                PatientInfo("Emily Anderson", 34, "Female", "Feb 20, 2026", "Hypertension", "Active"),
                PatientInfo("James Carter", 45, "Male", "Feb 18, 2026", "Diabetes Type 2", "Active"),
                PatientInfo("Sophia Williams", 28, "Female", "Feb 15, 2026", "Acute Hepatitis", "Critical"),
                PatientInfo("Michael Brown", 52, "Male", "Feb 12, 2026", "Cardiomegaly", "Active"),
                PatientInfo("Priya Sharma", 30, "Female", "Feb 10, 2026", "Hypothyroidism", "New"),
                PatientInfo("Ravi Kumar", 60, "Male", "Feb 8, 2026", "Arrhythmia", "Active"),
                PatientInfo("Ananya Patel", 22, "Female", "Jan 28, 2026", "Anemia", "Discharged"),
                PatientInfo("David Lee", 38, "Male", "Jan 20, 2026", "Pneumonia", "Discharged")
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
