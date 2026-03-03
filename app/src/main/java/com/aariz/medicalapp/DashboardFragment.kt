package com.aariz.medicalapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seeAll = view.findViewById<TextView>(R.id.tvSeeAllAppointments)

        seeAll.setOnClickListener {
            (activity as MainDoctorActivity)
                .loadFragment(ScheduleFragment(), "forward")
        }
    }
}