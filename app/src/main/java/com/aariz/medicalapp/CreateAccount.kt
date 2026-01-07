package com.aariz.medicalapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest

class CreateAccount : AppCompatActivity() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnCreateAccount: MaterialButton
    private lateinit var btnGoogle: MaterialButton
    private lateinit var tvSignIn: TextView

    // Firebase Authentication
    private lateinit var auth: FirebaseAuth

    // Google Sign-In
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    companion object {
        private const val TAG = "CreateAccount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        configureGoogleSignIn()

        // Initialize views
        initializeViews()

        // Setup Sign In clickable text
        setupSignInText()

        // Setup button click listeners
        setupClickListeners()
    }

    private fun configureGoogleSignIn() {
        // Configure Google Sign-In to request the user's ID, email, and basic profile
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun initializeViews() {
        tilName = findViewById(R.id.tilName)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)

        etName = tilName.editText as TextInputEditText
        etEmail = tilEmail.editText as TextInputEditText
        etPassword = tilPassword.editText as TextInputEditText

        btnCreateAccount = findViewById(R.id.btnCreateAccount)
        btnGoogle = findViewById(R.id.btnGoogle)
        tvSignIn = findViewById(R.id.tvSignIn)
    }

    private fun setupSignInText() {
        val fullText = "Do you have an account ? Sign In"
        val spannableString = SpannableString(fullText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                navigateToSignIn()
            }
        }

        val startIndex = fullText.indexOf("Sign In")
        val endIndex = startIndex + "Sign In".length

        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#1C64F2")),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvSignIn.text = spannableString
        tvSignIn.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupClickListeners() {
        btnCreateAccount.setOnClickListener {
            if (validateInputs()) {
                createAccount()
            }
        }

        btnGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun validateInputs(): Boolean {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        tilName.error = null
        tilEmail.error = null
        tilPassword.error = null

        if (name.isEmpty()) {
            tilName.error = "Name is required"
            etName.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            tilEmail.error = "Email is required"
            etEmail.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Please enter a valid email"
            etEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            tilPassword.error = "Password is required"
            etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            tilPassword.error = "Password must be at least 6 characters"
            etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun createAccount() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Show loading state
        setLoadingState(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    // Update user profile with display name
                    updateUserProfile(user, name)

                    // Send verification email
                    sendEmailVerification(user)

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    setLoadingState(false)

                    val errorMessage = when {
                        task.exception?.message?.contains("already in use") == true ->
                            "This email is already registered"
                        task.exception?.message?.contains("network") == true ->
                            "Network error. Please check your connection"
                        else -> task.exception?.message ?: "Registration failed"
                    }

                    Toast.makeText(
                        this,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun updateUserProfile(user: FirebaseUser?, name: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }

    private fun sendEmailVerification(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                setLoadingState(false)

                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Account created! Please complete your profile",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToProfileSetup()
                } else {
                    Toast.makeText(
                        this,
                        "Account created! Please complete your profile",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToProfileSetup()
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        setLoadingState(true)

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                setLoadingState(false)

                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                    // Check if this is a new user
                    val isNewUser = task.result?.additionalUserInfo?.isNewUser ?: false

                    if (isNewUser) {
                        Toast.makeText(
                            this,
                            "Welcome, ${user?.displayName}! Please complete your profile",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToProfileSetup()
                    } else {
                        Toast.makeText(
                            this,
                            "Welcome back, ${user?.displayName}!",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToHome()
                    }
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setLoadingState(isLoading: Boolean) {
        btnCreateAccount.isEnabled = !isLoading
        btnGoogle.isEnabled = !isLoading
        etName.isEnabled = !isLoading
        etEmail.isEnabled = !isLoading
        etPassword.isEnabled = !isLoading

        if (isLoading) {
            btnCreateAccount.text = "Creating..."
        } else {
            btnCreateAccount.text = "Create Account"
        }
    }

    private fun navigateToHome() {
        // Navigate to your main/home activity
        val intent = Intent(this, SplashScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToProfileSetup() {
        // Navigate to profile setup for new users
        val intent = Intent(this, ProfileSetupActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToSignIn() {
        // Navigate to Sign In screen
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already signed in, navigate to home
            navigateToHome()
        }
    }
}