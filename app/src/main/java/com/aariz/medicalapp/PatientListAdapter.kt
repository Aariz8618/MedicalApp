package com.aariz.medicalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PatientListAdapter(
    private val list: MutableList<PatientInfo>
) : RecyclerView.Adapter<PatientListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientName: TextView = view.findViewById(R.id.tvPatientName)
        val patientMeta: TextView = view.findViewById(R.id.tvPatientMeta)
        val lastVisit: TextView = view.findViewById(R.id.tvLastVisit)
        val condition: TextView = view.findViewById(R.id.tvCondition)
        val status: TextView = view.findViewById(R.id.tvPatientStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient_details_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val patient = list[position]
        holder.patientName.text = patient.name
        holder.patientMeta.text = "${patient.age} yrs • ${patient.gender}"
        holder.lastVisit.text = "Last visit: ${patient.lastVisit}"
        holder.condition.text = patient.condition
        holder.status.text = patient.status

        when (patient.status) {
            "Active" -> holder.status.setBackgroundResource(R.drawable.bg_status_analyzed)
            "New" -> holder.status.setBackgroundResource(R.drawable.bg_status_new_patient)
            "Discharged" -> holder.status.setBackgroundResource(R.drawable.bg_status_discharged)
            "Critical" -> holder.status.setBackgroundResource(R.drawable.bg_status_critical)
        }
    }
}
