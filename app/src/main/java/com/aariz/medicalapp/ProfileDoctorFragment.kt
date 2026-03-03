package com.aariz.medicalapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileDoctorFragment : Fragment(R.layout.fragment_profile_doctor) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener {

            // Load Dashboard
            (activity as MainDoctorActivity)
                .loadFragment(DashboardFragment(), "back")

            // Update Bottom Navigation selection
            requireActivity()
                .findViewById<BottomNavigationView>(R.id.bottom_navigation)
                .selectedItemId = R.id.nav_dashboard
        }
    }
}