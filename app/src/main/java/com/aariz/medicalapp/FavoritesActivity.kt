package com.aariz.medicalapp

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tabDoctors: TextView
    private lateinit var tabHospitals: TextView
    private lateinit var tabIndicator: View
    private lateinit var btnBack: ImageView
    private lateinit var topBar: View

    private lateinit var doctorAdapter: FavoriteDoctorAdapter
    private lateinit var hospitalAdapter: FavoriteHospitalAdapter

    private var currentTab = TAB_DOCTORS

    companion object {
        const val TAB_DOCTORS = 0
        const val TAB_HOSPITALS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_favorites)

        initViews()
        applyWindowInsets()
        setupRecyclerView()
        setupTabs()
        setupBackButton()

        // Position indicator after layout is complete
        tabDoctors.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tabDoctors.viewTreeObserver.removeOnGlobalLayoutListener(this)
                positionIndicator(tabDoctors)
            }
        })

        loadDoctors()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        tabDoctors = findViewById(R.id.tabDoctors)
        tabHospitals = findViewById(R.id.tabHospitals)
        tabIndicator = findViewById(R.id.tabIndicator)
        btnBack = findViewById(R.id.btnBack)
        topBar = findViewById(R.id.topBar)
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(topBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                systemBars.top + 16,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(recyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                systemBars.bottom + 24
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupBackButton() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapters
        doctorAdapter = FavoriteDoctorAdapter(getFavoriteDoctorsList())
        hospitalAdapter = FavoriteHospitalAdapter(getFavoriteHospitalsList())

        recyclerView.adapter = doctorAdapter
    }

    private fun setupTabs() {
        tabDoctors.setOnClickListener {
            if (currentTab != TAB_DOCTORS) {
                switchToTab(TAB_DOCTORS)
            }
        }

        tabHospitals.setOnClickListener {
            if (currentTab != TAB_HOSPITALS) {
                switchToTab(TAB_HOSPITALS)
            }
        }
    }

    private fun switchToTab(tab: Int) {
        currentTab = tab

        when (tab) {
            TAB_DOCTORS -> {
                // Update colors
                tabDoctors.setTextColor(ContextCompat.getColor(this, R.color.primary_dark))
                tabHospitals.setTextColor(ContextCompat.getColor(this, R.color.light_gray))

                // Animate indicator to Doctors tab
                animateIndicator(tabDoctors)

                // Switch adapter
                recyclerView.adapter = doctorAdapter
                loadDoctors()
            }
            TAB_HOSPITALS -> {
                // Update colors
                tabDoctors.setTextColor(ContextCompat.getColor(this, R.color.light_gray))
                tabHospitals.setTextColor(ContextCompat.getColor(this, R.color.primary_dark))

                // Animate indicator to Hospitals tab
                animateIndicator(tabHospitals)

                // Switch adapter
                recyclerView.adapter = hospitalAdapter
                loadHospitals()
            }
        }
    }

    private fun positionIndicator(tab: TextView) {
        val tabWidth = tab.width
        val tabLeft = tab.left

        // Center the indicator below the tab
        val indicatorWidth = tabIndicator.width
        val leftMargin = tabLeft + (tabWidth - indicatorWidth) / 2

        val params = tabIndicator.layoutParams as FrameLayout.LayoutParams
        params.leftMargin = leftMargin
        tabIndicator.layoutParams = params
    }

    private fun animateIndicator(targetTab: TextView) {
        val tabWidth = targetTab.width
        val tabLeft = targetTab.left

        // Calculate centered position
        val indicatorWidth = tabIndicator.width
        val targetLeftMargin = tabLeft + (tabWidth - indicatorWidth) / 2

        // Animate to new position
        tabIndicator.animate()
            .translationX(targetLeftMargin.toFloat() - tabIndicator.left.toFloat())
            .setDuration(200)
            .withEndAction {
                // Update actual layout params after animation
                val params = tabIndicator.layoutParams as FrameLayout.LayoutParams
                params.leftMargin = targetLeftMargin
                tabIndicator.layoutParams = params
                tabIndicator.translationX = 0f
            }
            .start()
    }

    private fun loadDoctors() {
        doctorAdapter.notifyDataSetChanged()
    }

    private fun loadHospitals() {
        hospitalAdapter.notifyDataSetChanged()
    }

    private fun getFavoriteDoctorsList(): List<Doctor> {
        return listOf(
            Doctor(
                "Dr. David Patel",
                "Cardiologist",
                "Cardiology Center, USA",
                5.0f,
                1872,
                R.drawable.doctor_male,
                true
            ),
            Doctor(
                "Dr. Jessica Turner",
                "Gynecologist",
                "Women's Clinic, Seattle, USA",
                4.9f,
                127,
                R.drawable.doctor_female,
                true
            ),
            Doctor(
                "Dr. Michael Johnson",
                "Orthopedic Surgery",
                "Maple Associates, NY, USA",
                4.7f,
                5223,
                R.drawable.doctor_male,
                true
            ),
            Doctor(
                "Dr. Emily Walker",
                "Pediatrics",
                "Serenity Pediatrics Clinic",
                5.0f,
                405,
                R.drawable.doctor_female,
                true
            ),
            Doctor(
                "Dr. Emma Green",
                "Gynecologist",
                "Bernard Clinic",
                5.0f,
                405,
                R.drawable.doctor_female,
                true
            )
        )
    }

    private fun getFavoriteHospitalsList(): List<Hospital> {
        return listOf(
            Hospital(
                "Sunrise Health Clinic",
                "123 Oak Street, CA 98765",
                5.0f,
                128,
                "2.5 km/40min",
                "Hospital",
                R.drawable.doctor_male
            ),
            Hospital(
                "Golden Cardiology Center",
                "555 Bridge Street, Golden Gate",
                4.9f,
                58,
                "2.5 km/40min",
                "Clinic",
                R.drawable.doctor_male
            ),
            Hospital(
                "Orthopedic Surgery Center",
                "555 Bridge Street, Golden Gate",
                4.9f,
                58,
                "2.5 km/40min",
                "Clinic",
                R.drawable.doctor_male
            )
        )
    }
}

data class Hospital(
    val name: String,
    val address: String,
    val rating: Float,
    val reviews: Int,
    val distance: String,
    val type: String,
    val imageRes: Int,
    var isFavorite: Boolean = true
)