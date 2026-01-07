package com.aariz.medicalapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var ivProfilePicture: CircleImageView
    private lateinit var btnEditPicture: ImageView
    private lateinit var tilFullName: TextInputLayout
    private lateinit var tilNickname: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilDateOfBirth: TextInputLayout
    private lateinit var tilGender: TextInputLayout
    private lateinit var etFullName: TextInputEditText
    private lateinit var etNickname: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etDateOfBirth: TextInputEditText
    private lateinit var actvGender: AutoCompleteTextView
    private lateinit var btnSave: MaterialButton

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var selectedDateOfBirth: String = ""
    private var successDialog: Dialog? = null

    companion object {
        private const val TAG = "ProfileSetupActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide status bar
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        setContentView(R.layout.activity_profile_setup)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        initializeViews()
        setupGenderDropdown()
        setupClickListeners()
        loadUserData()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        ivProfilePicture = findViewById(R.id.ivProfilePicture)
        btnEditPicture = findViewById(R.id.btnEditPicture)

        tilFullName = findViewById(R.id.tilFullName)
        tilNickname = findViewById(R.id.tilNickname)
        tilEmail = findViewById(R.id.tilEmail)
        tilDateOfBirth = findViewById(R.id.tilDateOfBirth)
        tilGender = findViewById(R.id.tilGender)

        etFullName = findViewById(R.id.etFullName)
        etNickname = findViewById(R.id.etNickname)
        etEmail = findViewById(R.id.etEmail)
        etDateOfBirth = findViewById(R.id.etDateOfBirth)
        actvGender = findViewById(R.id.actvGender)

        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Male", "Female", "Other", "Prefer not to say")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genders)
        actvGender.setAdapter(adapter)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnEditPicture.setOnClickListener {
            // Show coming soon toast for profile picture upload
            Toast.makeText(this, "Profile picture upload coming soon!", Toast.LENGTH_SHORT).show()
        }

        etDateOfBirth.setOnClickListener {
            showDatePicker()
        }

        btnSave.setOnClickListener {
            if (validateInputs()) {
                saveProfile()
            }
        }
    }

    private fun loadUserData() {
        val user = auth.currentUser
        user?.let {
            // Pre-fill email from Firebase Auth
            etEmail.setText(it.email)

            // Pre-fill name if available
            it.displayName?.let { name ->
                etFullName.setText(name)
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = Calendar.getInstance()
                date.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                selectedDateOfBirth = dateFormat.format(date.time)
                etDateOfBirth.setText(selectedDateOfBirth)
            },
            year,
            month,
            day
        )

        // Set max date to today (user must be born before today)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun validateInputs(): Boolean {
        val fullName = etFullName.text.toString().trim()
        val nickname = etNickname.text.toString().trim()
        val dateOfBirth = etDateOfBirth.text.toString().trim()
        val gender = actvGender.text.toString().trim()

        // Reset errors
        tilFullName.error = null
        tilNickname.error = null
        tilDateOfBirth.error = null
        tilGender.error = null

        if (fullName.isEmpty()) {
            tilFullName.error = "Full name is required"
            etFullName.requestFocus()
            return false
        }

        if (dateOfBirth.isEmpty()) {
            tilDateOfBirth.error = "Date of birth is required"
            etDateOfBirth.requestFocus()
            return false
        }

        if (gender.isEmpty()) {
            tilGender.error = "Please select your gender"
            actvGender.requestFocus()
            return false
        }

        return true
    }

    private fun saveProfile() {
        setLoadingState(true)

        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            setLoadingState(false)
            return
        }

        // Save profile without image (image upload will be added later)
        saveProfileData(null)
    }

    private fun saveProfileData(profileImageUrl: String?) {
        val user = auth.currentUser ?: return

        val fullName = etFullName.text.toString().trim()
        val nickname = etNickname.text.toString().trim()
        val dateOfBirth = etDateOfBirth.text.toString().trim()
        val gender = actvGender.text.toString().trim()

        // Create user profile data with explicit type
        val userProfile: HashMap<String, Any> = hashMapOf(
            "uid" to user.uid,
            "fullName" to fullName,
            "nickname" to nickname,
            "email" to (user.email ?: ""),
            "dateOfBirth" to dateOfBirth,
            "gender" to gender,
            "createdAt" to System.currentTimeMillis(),
            "profileCompleted" to true
        )

        // Add profile image URL if available
        profileImageUrl?.let {
            userProfile["profileImageUrl"] = it
        }

        // Save to Firestore
        saveToFirestore(userProfile, fullName)
    }

    private fun saveToFirestore(userProfile: HashMap<String, Any>, fullName: String) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                Log.d(TAG, "User profile saved successfully")

                // Update Firebase Auth display name
                updateAuthProfile(fullName)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to save user profile", exception)
                setLoadingState(false)
                Toast.makeText(
                    this,
                    "Failed to save profile: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun updateAuthProfile(displayName: String) {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                setLoadingState(false)

                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated in Auth")
                    showSuccessDialog()
                } else {
                    Toast.makeText(
                        this,
                        "Profile saved but failed to update auth",
                        Toast.LENGTH_SHORT
                    ).show()
                    showSuccessDialog()
                }
            }
    }

    private fun showSuccessDialog() {
        successDialog = Dialog(this)
        successDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        successDialog?.setContentView(R.layout.dialog_success)
        successDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        successDialog?.setCancelable(false)
        successDialog?.show()

        // Auto redirect after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            successDialog?.dismiss()
            navigateToHome()
        }, 3000)
    }

    private fun setLoadingState(isLoading: Boolean) {
        btnSave.isEnabled = !isLoading
        etFullName.isEnabled = !isLoading
        etNickname.isEnabled = !isLoading
        etDateOfBirth.isEnabled = !isLoading
        actvGender.isEnabled = !isLoading
        btnEditPicture.isEnabled = !isLoading

        if (isLoading) {
            btnSave.text = "Saving..."
        } else {
            btnSave.text = "Save"
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, SplashScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        successDialog?.dismiss()
    }
}