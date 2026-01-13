package com.aariz.medicalapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat

class DoctorDetailsActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var ivHeaderFavorite: ImageView
    private lateinit var ivDoctorImage: ImageView
    private lateinit var tvDoctorName: TextView
    private lateinit var tvSpecialty: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvViewMore: TextView
    private lateinit var tvSeeAll: TextView
    private lateinit var btnBookAppointment: AppCompatButton
    private lateinit var scrollView: ScrollView

    private var doctor: Doctor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_doctor_details)

        // Initialize views
        initializeViews()

        // Get doctor data from intent
        getDoctorData()

        // Apply window insets
        applyWindowInsets()

        // Set up back press handler
        setupBackPressHandler()

        // Set up click listeners
        setupClickListeners()

        // Populate UI with doctor data
        populateUI()
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sendBackResult()
                finish()
            }
        })
    }

    private fun initializeViews() {
        ivBack = findViewById(R.id.ivBack)
        ivHeaderFavorite = findViewById(R.id.ivHeaderFavorite)
        ivDoctorImage = findViewById(R.id.ivDoctorImage)
        tvDoctorName = findViewById(R.id.tvDoctorName)
        tvSpecialty = findViewById(R.id.tvSpecialty)
        tvLocation = findViewById(R.id.tvLocation)
        tvViewMore = findViewById(R.id.tvViewMore)
        tvSeeAll = findViewById(R.id.tvSeeAll)
        btnBookAppointment = findViewById(R.id.btnBookAppointment)
        scrollView = findViewById(R.id.scrollView)
    }

    private fun getDoctorData() {
        // Get doctor data passed from previous screen
        val name = intent.getStringExtra("DOCTOR_NAME")
        val specialty = intent.getStringExtra("DOCTOR_SPECIALTY")
        val location = intent.getStringExtra("DOCTOR_LOCATION")
        val rating = intent.getFloatExtra("DOCTOR_RATING", 0f)
        val reviewCount = intent.getIntExtra("DOCTOR_REVIEW_COUNT", 0)
        val imageResId = intent.getIntExtra("DOCTOR_IMAGE", R.drawable.doctor_male)
        val isFavorite = intent.getBooleanExtra("DOCTOR_FAVORITE", false)

        if (name != null && specialty != null && location != null) {
            doctor = Doctor(
                name = name,
                specialty = specialty,
                location = location,
                rating = rating,
                reviewCount = reviewCount,
                imageResId = imageResId,
                isFavorite = isFavorite
            )
        } else {
            // If no data passed, finish activity
            Toast.makeText(this, "Error loading doctor details", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        val bottomContainer = findViewById<android.widget.LinearLayout>(R.id.bottomContainer)
        ViewCompat.setOnApplyWindowInsetsListener(bottomContainer) { v, insets ->
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

    private fun setupClickListeners() {
        // Back button
        ivBack.setOnClickListener {
            sendBackResult()
            finish()
        }

        // Favorite button
        ivHeaderFavorite.setOnClickListener {
            doctor?.let {
                it.isFavorite = !it.isFavorite
                updateFavoriteIcon()
                val message = if (it.isFavorite) {
                    "Added to favorites"
                } else {
                    "Removed from favorites"
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // View more button
        tvViewMore.setOnClickListener {
            Toast.makeText(this, "View more about doctor", Toast.LENGTH_SHORT).show()
        }

        // See all reviews button
        tvSeeAll.setOnClickListener {
            Toast.makeText(this, "View all reviews", Toast.LENGTH_SHORT).show()
        }

        // Book appointment button
        btnBookAppointment.setOnClickListener {
            doctor?.let {
                Toast.makeText(
                    this,
                    "Booking appointment with ${it.name}",
                    Toast.LENGTH_SHORT
                ).show()
                // Navigate to appointment booking screen
            }
        }
    }

    private fun populateUI() {
        doctor?.let {
            // Set doctor image
            ivDoctorImage.setImageResource(it.imageResId)

            // Set doctor name
            tvDoctorName.text = it.name

            // Set specialty
            tvSpecialty.text = it.specialty

            // Set location
            tvLocation.text = it.location

            // Add underline to "view more" text
            tvViewMore.paintFlags = tvViewMore.paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG

            // Update favorite icon
            updateFavoriteIcon()
        }
    }

    private fun updateFavoriteIcon() {
        doctor?.let {
            val heartIcon = if (it.isFavorite) {
                R.drawable.ic_heart_filled
            } else {
                R.drawable.ic_heart
            }
            ivHeaderFavorite.setImageResource(heartIcon)
        }
    }

    private fun sendBackResult() {
        // Send back the updated favorite status
        doctor?.let {
            val resultIntent = android.content.Intent().apply {
                putExtra("DOCTOR_FAVORITE", it.isFavorite)
                putExtra("DOCTOR_NAME", it.name)
            }
            setResult(RESULT_OK, resultIntent)
        }
    }
}