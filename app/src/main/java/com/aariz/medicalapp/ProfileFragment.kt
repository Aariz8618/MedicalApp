package com.aariz.medicalapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        loadUserProfile(view)
        setupClickListeners(view)
    }

    private fun loadUserProfile(view: View) {
        val user = auth.currentUser
        val tvName = view.findViewById<android.widget.TextView>(R.id.tvProfileName)
        val tvPhone = view.findViewById<android.widget.TextView>(R.id.tvProfilePhone)

        user?.let {
            // Load from Firestore
            firestore.collection("users")
                .document(it.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val fullName = document.getString("fullName") ?: it.displayName ?: "User"
                        val phone = document.getString("phone") ?: "+123 856479683"

                        tvName.text = fullName
                        tvPhone.text = phone
                    } else {
                        tvName.text = it.displayName ?: "User"
                        tvPhone.text = "+123 856479683"
                    }
                }
                .addOnFailureListener {
                    tvName.text = user.displayName ?: "User"
                    tvPhone.text = "+123 856479683"
                }
        }
    }

    private fun setupClickListeners(view: View) {
        // Edit Profile
        view.findViewById<View>(R.id.itemEditProfile)?.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show()
        }

        // Favorite - Navigate to FavoritesActivity
        view.findViewById<View>(R.id.itemFavorite)?.setOnClickListener {
            navigateToFavorites()
        }

        // Notifications
        view.findViewById<View>(R.id.itemNotifications)?.setOnClickListener {
            Toast.makeText(requireContext(), "Notifications", Toast.LENGTH_SHORT).show()
        }

        // Settings
        view.findViewById<View>(R.id.itemSettings)?.setOnClickListener {
            Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
        }

        // Help and Support
        view.findViewById<View>(R.id.itemHelpSupport)?.setOnClickListener {
            Toast.makeText(requireContext(), "Help and Support", Toast.LENGTH_SHORT).show()
        }

        // Terms and Conditions
        view.findViewById<View>(R.id.itemTerms)?.setOnClickListener {
            Toast.makeText(requireContext(), "Terms and Conditions", Toast.LENGTH_SHORT).show()
        }

        // Log Out
        view.findViewById<View>(R.id.itemLogout)?.setOnClickListener {
            showLogoutDialog()
        }

        // Edit Picture
        view.findViewById<View>(R.id.btnEditPicture)?.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Picture", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToFavorites() {
        val intent = Intent(requireContext(), FavoritesActivity::class.java)
        startActivity(intent)
    }

    private fun showLogoutDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        bottomSheetDialog.setContentView(dialogView)

        // Set rounded corners
        bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Cancel button
        dialogView.findViewById<View>(R.id.btnCancel)?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Logout button
        dialogView.findViewById<View>(R.id.btnLogout)?.setOnClickListener {
            bottomSheetDialog.dismiss()
            performLogout()
        }

        bottomSheetDialog.show()
    }

    private fun performLogout() {
        auth.signOut()

        // Clear onboarding preference
        val prefs = requireActivity().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        // Navigate to SignIn
        val intent = Intent(requireActivity(), SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}