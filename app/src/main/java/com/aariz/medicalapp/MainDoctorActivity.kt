package com.aariz.medicalapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainDoctorActivity : AppCompatActivity() {

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_doctor)

        loadFragment(DashboardFragment(), "forward")

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener {

            val newIndex = when (it.itemId) {
                R.id.nav_dashboard -> 0
                R.id.nav_reports -> 1
                R.id.nav_patients -> 2
                R.id.nav_profile -> 3
                else -> 0
            }

            val direction = if (newIndex > currentIndex) "forward" else "back"

            val fragment = when (it.itemId) {
                R.id.nav_dashboard -> DashboardFragment()
                R.id.nav_reports -> ReportsFragment()
                R.id.nav_patients -> PatientsFragment()
                R.id.nav_profile -> ProfileDoctorFragment()
                else -> DashboardFragment()
            }

            loadFragment(fragment, direction)

            currentIndex = newIndex

            true
        }
    }

    fun loadFragment(fragment: Fragment, direction: String) {

        val transaction = supportFragmentManager.beginTransaction()

        if (direction == "forward") {
            transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        } else {
            transaction.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }

        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
