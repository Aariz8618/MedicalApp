package com.aariz.medicalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class PatientListAdapter(
    private val list: MutableList<PatientInfo>,
    private val onItemClick: ((PatientInfo) -> Unit)? = null
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
        val ctx = holder.itemView.context
        holder.patientName.text = patient.name
        holder.patientMeta.text = "${patient.age} yrs • ${patient.gender}"
        holder.lastVisit.text = "Last visit: ${patient.lastVisit}"
        holder.condition.text = patient.condition
        holder.status.text = patient.status

        when (patient.status) {
            "Active" -> {
                holder.status.setBackgroundResource(R.drawable.bg_status_active)
                holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.status_active_text))
            }
            "New" -> {
                holder.status.setBackgroundResource(R.drawable.bg_status_new_pill)
                holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.status_new_text))
            }
            "Discharged" -> {
                holder.status.setBackgroundResource(R.drawable.bg_status_discharged_pill)
                holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.status_discharged_text))
            }
            "Critical" -> {
                holder.status.setBackgroundResource(R.drawable.bg_status_critical_pill)
                holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.status_critical_text))
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(patient)
        }
    }
}
