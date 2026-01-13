package com.aariz.medicalapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        // Notification icon click
        view.findViewById<View>(R.id.notification_icon)?.apply {
            setOnClickListener {
                animateClick(this)
                Toast.makeText(requireContext(), "Notifications", Toast.LENGTH_SHORT).show()
            }
        }

        // Location click
        view.findViewById<View>(R.id.location_container)?.apply {
            setOnClickListener {
                animateClick(this)
                Toast.makeText(requireContext(), "Change Location", Toast.LENGTH_SHORT).show()
            }
        }

        // Search bar click
        view.findViewById<View>(R.id.search_bar)?.apply {
            setOnClickListener {
                animateClick(this)
                navigateToDoctorList()
            }
        }

        // Categories "See All" click
        view.findViewById<View>(R.id.categories_see_all)?.apply {
            setOnClickListener {
                animateClick(this)
                navigateToDoctorList()
            }
        }

        // Medical Centers "See All" click
        view.findViewById<View>(R.id.medical_centers_see_all)?.apply {
            setOnClickListener {
                animateClick(this)
                Toast.makeText(requireContext(), "View All Medical Centers", Toast.LENGTH_SHORT).show()
            }
        }

        setupCategoryClickListeners(view)
        setupMedicalCenterClickListeners(view)
    }

    private fun setupCategoryClickListeners(view: View) {
        val categoryData = listOf(
            Pair(R.id.category_dentistry, "Dentistry"),
            Pair(R.id.category_cardiology, "Cardiology"),
            Pair(R.id.category_pulmonology, "Pulmonology"),
            Pair(R.id.category_general, "General"),
            Pair(R.id.category_neurology, "Neurology"),
            Pair(R.id.category_gastro, "Gastroenterology"),
            Pair(R.id.category_laboratory, "Laboratory"),
            Pair(R.id.category_vaccination, "Vaccination")
        )

        categoryData.forEach { (id, name) ->
            view.findViewById<View>(id)?.apply {
                setOnClickListener {
                    animateClick(this)
                    navigateToDoctorList(name)
                }
            }
        }
    }

    private fun setupMedicalCenterClickListeners(view: View) {
        view.findViewById<View>(R.id.card_sunrise)?.apply {
            setOnClickListener {
                animateCardClick(this)
                Toast.makeText(requireContext(), "Sunrise Health Clinic", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<View>(R.id.card_golden)?.apply {
            setOnClickListener {
                animateCardClick(this)
                Toast.makeText(requireContext(), "Golden Cardiology Center", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun animateClick(view: View) {
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun animateCardClick(view: View) {
        view.animate()
            .scaleX(0.97f)
            .scaleY(0.97f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun navigateToDoctorList(category: String? = null) {
        val intent = Intent(requireContext(), DoctorListActivity::class.java)
        category?.let {
            intent.putExtra("CATEGORY", it)
        }
        startActivity(intent)
    }
}