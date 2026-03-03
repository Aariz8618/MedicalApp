package com.aariz.medicalapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReportDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_detail)

        val reportId = intent.getStringExtra("report_id") ?: "1"

        val report = getReportById(reportId)

        report?.let { bindReport(it) }

        findViewById<FrameLayout>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val tvExpandOcr = findViewById<TextView>(R.id.tvExpandOcr)
        val tvExtractedText = findViewById<TextView>(R.id.tvExtractedText)

        tvExpandOcr.setOnClickListener {
            if (tvExtractedText.visibility == View.GONE) {
                tvExtractedText.visibility = View.VISIBLE
                tvExpandOcr.text = "Hide"
            } else {
                tvExtractedText.visibility = View.GONE
                tvExpandOcr.text = "Show"
            }
        }

        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnSaveNotes)
            .setOnClickListener {
                Toast.makeText(this, "Notes saved successfully", Toast.LENGTH_SHORT).show()
            }

        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnShareWithPatient)
            .setOnClickListener {
                Toast.makeText(this, "Report shared with patient", Toast.LENGTH_SHORT).show()
            }

        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnDownloadPdf)
            .setOnClickListener {
                Toast.makeText(this, "Downloading report PDF...", Toast.LENGTH_SHORT).show()
            }
    }

    private fun bindReport(report: Report) {
        findViewById<TextView>(R.id.tvDetailPatientName).text = report.patientName
        findViewById<TextView>(R.id.tvDetailReportType).text = report.reportType
        findViewById<TextView>(R.id.tvDetailDate).text = report.date
        findViewById<TextView>(R.id.tvExtractedText).text = report.extractedText
        if (report.summary.isNotEmpty()) {
            findViewById<TextView>(R.id.tvClinicalSummary).text = report.summary
        }

        val statusView = findViewById<TextView>(R.id.tvDetailStatus)
        statusView.text = report.status
        when (report.status) {
            "Pending" -> statusView.setBackgroundResource(R.drawable.bg_status_pending)
            "Analyzed" -> statusView.setBackgroundResource(R.drawable.bg_status_analyzed)
            "Critical" -> statusView.setBackgroundResource(R.drawable.bg_status_critical)
        }
    }

    private fun getReportById(id: String): Report? {
        val reports = listOf(
            Report(
                id = "1",
                patientName = "Emily Anderson",
                reportType = "Blood Test Report",
                reportCategory = "Lab Report",
                date = "Feb 20, 2026",
                status = "Analyzed",
                summary = "CBC and metabolic panel within normal range except elevated WBC count (11.5 K/uL) which suggests a possible mild bacterial or viral infection. Hemoglobin and platelet counts are within acceptable limits. Recommend follow-up with CRP and ESR tests to determine infection etiology.",
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
        return reports.find { it.id == id }
    }
}
