package com.aariz.medicalapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 1500L
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Enable edge-to-edge display
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Show splash screen for 1500ms, then decide where to navigate
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, SPLASH_DELAY)
    }

    private fun navigateToNextScreen() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val hasSeenOnboarding = prefs.getBoolean("onboarding_complete", false)
        val currentUser = auth.currentUser

        when {
            // User not logged in and hasn't seen onboarding -> Show Onboarding
            currentUser == null && !hasSeenOnboarding -> {
                navigateToOnboarding()
            }
            // User not logged in but has seen onboarding -> Show Sign In
            currentUser == null && hasSeenOnboarding -> {
                navigateToSignIn()
            }
            // User logged in but email not verified -> Show Sign In
            currentUser != null && !currentUser.isEmailVerified -> {
                auth.signOut()
                navigateToSignIn()
            }
            // User logged in and verified -> Check if profile is complete
            currentUser != null && currentUser.isEmailVerified -> {
                checkProfileCompletion(currentUser.uid)
            }
            // Default case -> Show Onboarding
            else -> {
                navigateToOnboarding()
            }
        }
    }

    private fun checkProfileCompletion(userId: String) {
        // Check if user has completed their profile setup
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists() && document.getBoolean("profileCompleted") == true) {
                    // Profile is complete -> Navigate to Home (you'll create this later)
                    navigateToHome()
                } else {
                    // Profile not complete -> Navigate to Profile Setup
                    navigateToProfileSetup()
                }
            }
            .addOnFailureListener {
                // On error, assume profile not complete
                navigateToProfileSetup()
            }
    }

    private fun navigateToOnboarding() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToProfileSetup() {
        val intent = Intent(this, ProfileSetupActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {

        android.widget.Toast.makeText(
            this,
            "Home screen coming soon! For now, signing out...",
            android.widget.Toast.LENGTH_SHORT
        ).show()
        auth.signOut()
        navigateToSignIn()
    }
}