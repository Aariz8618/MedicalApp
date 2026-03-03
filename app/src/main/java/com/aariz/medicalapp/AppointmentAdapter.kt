package com.aariz.medicalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter(private val list: MutableList<Appointment>) :
    RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientName: TextView = view.findViewById(R.id.tvPatientName)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val type: TextView = view.findViewById(R.id.tvAppointmentType)
        val time: TextView = view.findViewById(R.id.tvAppointmentTime)
        val mode: TextView = view.findViewById(R.id.tvMode)
        val duration: TextView = view.findViewById(R.id.tvDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = list[position]

        holder.patientName.text = appointment.patientName
        holder.type.text = appointment.type
        holder.time.text = appointment.dateTime
        holder.status.text = appointment.status
        holder.mode.text = "In-Person"     // Static for now
        holder.duration.text = "30 min"    // Static for now
    }
}