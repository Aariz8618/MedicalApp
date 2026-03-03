package com.aariz.medicalapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvSeeAllAppointments).setOnClickListener {
            (activity as MainDoctorActivity)
                .loadFragment(ScheduleFragment(), "forward")
        }

        view.findViewById<View>(R.id.btnReports).setOnClickListener {
            navigateToReports()
        }

        view.findViewById<View>(R.id.btnMyPatients).setOnClickListener {
            navigateToPatients()
        }

        view.findViewById<TextView>(R.id.tvSeeAllReports).setOnClickListener {
            navigateToReports()
        }
    }

    private fun navigateToReports() {
        (activity as MainDoctorActivity).loadFragment(ReportsFragment(), "forward")
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .selectedItemId = R.id.nav_reports
    }

    private fun navigateToPatients() {
        (activity as MainDoctorActivity).loadFragment(PatientsFragment(), "forward")
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .selectedItemId = R.id.nav_patients
    }
}
