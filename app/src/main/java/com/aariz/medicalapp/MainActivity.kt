package com.aariz.medicalapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display (modern approach)
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val fragmentContainer = findViewById<android.widget.FrameLayout>(R.id.fragment_container)

        // Apply window insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                0,
                systemBars.top,
                0,
                0 // Bottom padding handled by bottom navigation
            )
            insets
        }

        // Apply bottom padding to bottom navigation
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layoutParams = v.layoutParams
            layoutParams.height = v.minimumHeight + systemBars.bottom
            v.layoutParams = layoutParams
            v.setPadding(
                0,
                0,
                0,
                systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        // Load home fragment by default
        loadFragment(HomeFragment())

        // Set up bottom navigation listener
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_location -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                    false // Don't select this item
                }
                R.id.nav_appointment -> {
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                    false // Don't select this item
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}