package com.aariz.medicalapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class PatientsFragment : Fragment(R.layout.my_patients) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<FrameLayout>(R.id.btnBack)

        btnBack.setOnClickListener {
            (activity as MainDoctorActivity)
                .loadFragment(DashboardFragment(), "back")
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                .selectedItemId = R.id.nav_dashboard
        }
    }
}