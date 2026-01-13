package com.aariz.medicalapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DoctorListActivity : AppCompatActivity(), DoctorAdapter.OnDoctorClickListener {

    private lateinit var recyclerViewDoctors: RecyclerView
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var allDoctorsList: MutableList<Doctor>
    private lateinit var filteredDoctorsList: MutableList<Doctor>
    private lateinit var tvResultCount: TextView
    private lateinit var btnBack: FrameLayout
    private lateinit var headerSection: LinearLayout
    private lateinit var searchBar: CardView
    private lateinit var searchEditText: EditText

    // Tab TextViews
    private lateinit var tabAll: TextView
    private lateinit var tabGeneral: TextView
    private lateinit var tabCardiologist: TextView
    private lateinit var tabDentist: TextView
    private lateinit var tabNeurologist: TextView

    private var selectedTab = "All"
    private var searchQuery = ""

    companion object {
        private const val REQUEST_DOCTOR_DETAILS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_doctor_list)

        // Initialize views
        recyclerViewDoctors = findViewById(R.id.recyclerViewDoctors)
        tvResultCount = findViewById(R.id.tvResultCount)
        btnBack = findViewById(R.id.btnBack)
        headerSection = findViewById(R.id.header_section)
        searchBar = findViewById(R.id.search_bar)
        searchEditText = findViewById(R.id.searchEditText)

        // Initialize tab views
        tabAll = findViewById(R.id.tab_all)
        tabGeneral = findViewById(R.id.tab_general)
        tabCardiologist = findViewById(R.id.tab_cardiologist)
        tabDentist = findViewById(R.id.tab_dentist)
        tabNeurologist = findViewById(R.id.tab_neurologist)

        // Apply window insets
        applyWindowInsets()

        // Set up back button
        btnBack.setOnClickListener {
            finish()
        }

        // Set up RecyclerView
        recyclerViewDoctors.layoutManager = LinearLayoutManager(this)

        // Initialize doctor list with sample data
        allDoctorsList = mutableListOf(
            Doctor("Dr. David Patel", "Cardiologist", "Cardiology Center, USA", 5.0f, 1872, R.drawable.doctor_male),
            Doctor("Dr. Jessica Turner", "Gynecologist", "Women's Clinic, Seatle, USA", 4.9f, 127, R.drawable.doctor_female),
            Doctor("Dr. Michael Johnson", "Orthopedic Surgery", "Maple Associates, NY, USA", 4.7f, 5223, R.drawable.doctor_male),
            Doctor("Dr. Emily Walker", "Pediatrics", "Serenity Pediatrics Clinic", 5.0f, 405, R.drawable.doctor_female),
            Doctor("Dr. Sarah Brown", "General", "City Hospital, USA", 4.8f, 320, R.drawable.doctor_female),
            Doctor("Dr. James Wilson", "Dentist", "Smile Dental Care, USA", 4.9f, 567, R.drawable.doctor_male),
            Doctor("Dr. Robert Chen", "Neurologist", "Brain Health Center, USA", 5.0f, 890, R.drawable.doctor_male),
            Doctor("Dr. Maria Garcia", "Cardiologist", "Heart Care Institute, USA", 4.7f, 1200, R.drawable.doctor_female)
        )

        filteredDoctorsList = allDoctorsList.toMutableList()

        // Set up adapter
        doctorAdapter = DoctorAdapter(filteredDoctorsList, this)
        recyclerViewDoctors.adapter = doctorAdapter

        // Set up search functionality
        setupSearchFunctionality()

        // Set up tab click listeners
        setupTabClickListeners()

        // Check if category was passed from HomeFragment
        val category = intent.getStringExtra("CATEGORY")
        if (category != null) {
            filterByCategory(category)
        } else {
            updateResultCount()
        }
    }

    private fun setupSearchFunctionality() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()
                applyFilters()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun applyWindowInsets() {
        // Apply top padding to header section
        ViewCompat.setOnApplyWindowInsetsListener(headerSection) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        // Apply bottom padding to RecyclerView
        ViewCompat.setOnApplyWindowInsetsListener(recyclerViewDoctors) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupTabClickListeners() {
        tabAll.setOnClickListener {
            selectTab("All", tabAll)
            filterDoctors("All")
        }

        tabGeneral.setOnClickListener {
            selectTab("General", tabGeneral)
            filterDoctors("General")
        }

        tabCardiologist.setOnClickListener {
            selectTab("Cardiologist", tabCardiologist)
            filterDoctors("Cardiologist")
        }

        tabDentist.setOnClickListener {
            selectTab("Dentist", tabDentist)
            filterDoctors("Dentist")
        }

        tabNeurologist.setOnClickListener {
            selectTab("Neurologist", tabNeurologist)
            filterDoctors("Neurologist")
        }
    }

    private fun selectTab(tabName: String, selectedTabView: TextView) {
        selectedTab = tabName

        // Reset all tabs to unselected state
        val tabs = listOf(tabAll, tabGeneral, tabCardiologist, tabDentist, tabNeurologist)
        tabs.forEach { tab ->
            tab.setBackgroundResource(R.drawable.tab_unselected_bg)
            tab.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected_text))
        }

        // Set selected tab style
        selectedTabView.setBackgroundResource(R.drawable.tab_selected_bg)
        selectedTabView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    }

    private fun filterDoctors(category: String) {
        selectedTab = category
        applyFilters()
    }

    private fun applyFilters() {
        filteredDoctorsList.clear()

        // Start with all doctors or filtered by category
        val categoryFiltered = if (selectedTab == "All") {
            allDoctorsList
        } else {
            allDoctorsList.filter { it.specialty.equals(selectedTab, ignoreCase = true) }
        }

        // Apply search filter if there's a search query
        val searchFiltered = if (searchQuery.isNotEmpty()) {
            categoryFiltered.filter { doctor ->
                doctor.name.contains(searchQuery, ignoreCase = true) ||
                        doctor.specialty.contains(searchQuery, ignoreCase = true) ||
                        doctor.location.contains(searchQuery, ignoreCase = true)
            }
        } else {
            categoryFiltered
        }

        filteredDoctorsList.addAll(searchFiltered)
        doctorAdapter.notifyDataSetChanged()
        updateResultCount()
    }

    private fun filterByCategory(category: String) {
        when (category) {
            "Cardiology" -> {
                selectTab("Cardiologist", tabCardiologist)
                filterDoctors("Cardiologist")
            }
            "General" -> {
                selectTab("General", tabGeneral)
                filterDoctors("General")
            }
            "Dentistry" -> {
                selectTab("Dentist", tabDentist)
                filterDoctors("Dentist")
            }
            "Neurology" -> {
                selectTab("Neurologist", tabNeurologist)
                filterDoctors("Neurologist")
            }
            else -> {
                selectTab("All", tabAll)
                filterDoctors("All")
            }
        }
    }

    private fun updateResultCount() {
        tvResultCount.text = "${filteredDoctorsList.size} founds"
    }

    override fun onDoctorClick(doctor: Doctor) {
        // Navigate to doctor details screen
        val intent = Intent(this, DoctorDetailsActivity::class.java).apply {
            putExtra("DOCTOR_NAME", doctor.name)
            putExtra("DOCTOR_SPECIALTY", doctor.specialty)
            putExtra("DOCTOR_LOCATION", doctor.location)
            putExtra("DOCTOR_RATING", doctor.rating)
            putExtra("DOCTOR_REVIEW_COUNT", doctor.reviewCount)
            putExtra("DOCTOR_IMAGE", doctor.imageResId)
            putExtra("DOCTOR_FAVORITE", doctor.isFavorite)
        }
        startActivityForResult(intent, REQUEST_DOCTOR_DETAILS)
    }

    override fun onFavoriteClick(doctor: Doctor, position: Int) {
        val message = if (doctor.isFavorite) "Added to favorites" else "Removed from favorites"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_DOCTOR_DETAILS && resultCode == RESULT_OK) {
            // Update favorite status if changed
            data?.let {
                val isFavorite = it.getBooleanExtra("DOCTOR_FAVORITE", false)
                val doctorName = it.getStringExtra("DOCTOR_NAME")

                // Find and update the doctor in the list
                allDoctorsList.find { doctor -> doctor.name == doctorName }?.let { doctor ->
                    doctor.isFavorite = isFavorite
                }

                // Refresh the filtered list
                applyFilters()
            }
        }
    }
}