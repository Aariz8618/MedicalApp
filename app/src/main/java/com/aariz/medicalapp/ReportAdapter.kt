package com.aariz.medicalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ReportAdapter(
    private val list: MutableList<Report>,
    private val onItemClick: (Report) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientName: TextView = view.findViewById(R.id.tvPatientName)
        val reportType: TextView = view.findViewById(R.id.tvReportType)
        val reportDate: TextView = view.findViewById(R.id.tvReportDate)
        val reportCategory: TextView = view.findViewById(R.id.tvReportCategory)
        val reportStatus: TextView = view.findViewById(R.id.tvReportStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = list[position]
        holder.patientName.text = report.patientName
        holder.reportType.text = report.reportType
        holder.reportDate.text = report.date
        holder.reportCategory.text = report.reportCategory
        holder.reportStatus.text = report.status

        val context = holder.itemView.context
        when (report.status) {
            "Pending" -> holder.reportStatus.setBackgroundResource(R.drawable.bg_status_pending)
            "Analyzed" -> holder.reportStatus.setBackgroundResource(R.drawable.bg_status_analyzed)
            "Critical" -> holder.reportStatus.setBackgroundResource(R.drawable.bg_status_critical)
        }

        holder.itemView.setOnClickListener { onItemClick(report) }
    }
}
