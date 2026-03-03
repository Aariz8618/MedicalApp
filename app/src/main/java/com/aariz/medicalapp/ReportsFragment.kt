package com.aariz.medicalapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReportsFragment : Fragment(R.layout.fragment_reports) {

    private lateinit var adapter: ReportAdapter
    private val allReports = mutableListOf<Report>()
    private val filteredReports = mutableListOf<Report>()
    private var currentFilter = "All"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDummyData()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewReports)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReportAdapter(filteredReports) { report ->
            val intent = Intent(requireContext(), ReportDetailActivity::class.java)
            intent.putExtra("report_id", report.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        updateFilteredList(view)

        val tabAll = view.findViewById<TextView>(R.id.tab_all)
        val tabPending = view.findViewById<TextView>(R.id.tab_pending)
        val tabAnalyzed = view.findViewById<TextView>(R.id.tab_analyzed)
        val tabCritical = view.findViewById<TextView>(R.id.tab_critical)
        val tabs = listOf(tabAll, tabPending, tabAnalyzed, tabCritical)

        tabs.forEach { tab ->
            tab.setOnClickListener {
                tabs.forEach { t ->
                    t.setBackgroundResource(R.drawable.tab_unselected_bg)
                    t.setTextColor(resources.getColor(R.color.primary_dark, null))
                }
                tab.setBackgroundResource(R.drawable.tab_selected_bg)
                tab.setTextColor(resources.getColor(android.R.color.white, null))
                currentFilter = tab.text.toString()
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
        allReports.addAll(
            listOf(
                Report(
                    id = "1",
                    patientName = "Emily Anderson",
                    reportType = "Blood Test Report",
                    reportCategory = "Lab Report",
                    date = "Feb 20, 2026",
                    status = "Analyzed",
                    summary = "CBC and metabolic panel within normal range except elevated WBC count suggesting mild infection.",
                    extractedText = "Patient: Emily Anderson\nDate: 20/02/2026\nHemoglobin: 13.2 g/dL\nWBC: 11.5 K/uL\nPlatelets: 245 K/uL\nGlucose: 98 mg/dL\nCreatinine: 0.9 mg/dL",
                    abnormalValues = listOf(
                        AbnormalValue("WBC Count", "11.5 K/uL", "4.0 - 10.0 K/uL", true)
                    )
                ),
                Report(
                    id = "2",
                    patientName = "James Carter",
                    reportType = "MRI Scan",
                    reportCategory = "Imaging",
                    date = "Feb 18, 2026",
                    status = "Pending",
                    summary = "Brain MRI scan pending radiologist review.",
                    extractedText = "Patient: James Carter\nDate: 18/02/2026\nImaging: Brain MRI\nContrast: No\nFindings: Pending review"
                ),
                Report(
                    id = "3",
                    patientName = "Sophia Williams",
                    reportType = "Liver Function Test",
                    reportCategory = "Lab Report",
                    date = "Feb 15, 2026",
                    status = "Critical",
                    summary = "Severely elevated liver enzymes indicating acute hepatic stress. Immediate intervention required.",
                    extractedText = "Patient: Sophia Williams\nDate: 15/02/2026\nALT: 245 U/L\nAST: 198 U/L\nALP: 156 U/L\nBilirubin: 3.2 mg/dL",
                    abnormalValues = listOf(
                        AbnormalValue("ALT", "245 U/L", "7 - 56 U/L", true),
                        AbnormalValue("AST", "198 U/L", "10 - 40 U/L", true),
                        AbnormalValue("Bilirubin", "3.2 mg/dL", "0.1 - 1.2 mg/dL", true)
                    )
                ),
                Report(
                    id = "4",
                    patientName = "Michael Brown",
                    reportType = "X-Ray Chest",
                    reportCategory = "Imaging",
                    date = "Feb 12, 2026",
                    status = "Analyzed",
                    summary = "Chest X-ray shows mild cardiomegaly. No active pulmonary lesions detected.",
                    extractedText = "Patient: Michael Brown\nDate: 12/02/2026\nImaging: Chest X-Ray PA View\nFindings: Mild cardiomegaly\nLungs: Clear\nDiaphragm: Normal"
                ),
                Report(
                    id = "5",
                    patientName = "Priya Sharma",
                    reportType = "Thyroid Profile",
                    reportCategory = "Lab Report",
                    date = "Feb 10, 2026",
                    status = "Pending",
                    summary = "Thyroid function tests ordered. Results pending.",
                    extractedText = "Patient: Priya Sharma\nDate: 10/02/2026\nTSH: Pending\nT3: Pending\nT4: Pending"
                ),
                Report(
                    id = "6",
                    patientName = "Ravi Kumar",
                    reportType = "ECG Report",
                    reportCategory = "Cardiology",
                    date = "Feb 8, 2026",
                    status = "Analyzed",
                    summary = "12-lead ECG shows sinus rhythm. No significant ST changes. Heart rate 72 bpm.",
                    extractedText = "Patient: Ravi Kumar\nDate: 08/02/2026\nRhythm: Sinus Rhythm\nHR: 72 bpm\nQRS: 0.08 sec\nQT: 0.40 sec\nST Segment: Normal"
                )
            )
        )
    }

    private fun updateFilteredList(view: View) {
        filteredReports.clear()
        filteredReports.addAll(
            if (currentFilter == "All") allReports
            else allReports.filter { it.status == currentFilter }
        )
        view.findViewById<TextView>(R.id.tvReportCount)?.text = "${filteredReports.size} reports"
        adapter.notifyDataSetChanged()
    }

    private fun filterBySearch(view: View, query: String) {
        filteredReports.clear()
        val base = if (currentFilter == "All") allReports else allReports.filter { it.status == currentFilter }
        filteredReports.addAll(
            if (query.isEmpty()) base
            else base.filter {
                it.patientName.contains(query, ignoreCase = true) ||
                        it.reportType.contains(query, ignoreCase = true)
            }
        )
        view.findViewById<TextView>(R.id.tvReportCount)?.text = "${filteredReports.size} reports"
        adapter.notifyDataSetChanged()
    }
}
