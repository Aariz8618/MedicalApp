package com.aariz.medicalapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

data class OnboardingItem(
    val imageRes: Int,
    val title: String,
    val description: String
)

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorLayout: LinearLayout
    private lateinit var tvSkip: TextView
    private lateinit var bottomContainer: LinearLayout

    private val onboardingItems = listOf(
        OnboardingItem(
            R.drawable.doctor_1,
            "Meet Doctors Online",
            "Connect with Specialized Doctors Online for Convenient and Comprehensive Medical Consultations."
        ),
        OnboardingItem(
            R.drawable.doctor_2,
            "Connect with Specialists",
            "Connect with Specialized Doctors Online for Convenient and Comprehensive Medical Consultations."
        ),
        OnboardingItem(
            R.drawable.doctor_3,
            "Thousands of Online Specialists",
            "Explore a Vast Array of Online Medical Specialists, Offering an Extensive Range of Expertise Tailored to Your Healthcare Needs."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display BEFORE setContentView
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_onboarding)

        // Make status bar transparent
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        // Set status bar color to transparent
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        val rootLayout = findViewById<View>(R.id.rootLayout)
        viewPager = findViewById(R.id.viewPager)
        indicatorLayout = findViewById(R.id.indicatorLayout)
        tvSkip = findViewById(R.id.tvSkip)
        bottomContainer = findViewById(R.id.bottomContainer)

        // Apply bottom padding for navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(bottomContainer) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                systemBars.bottom
            )
            insets
        }

        val adapter = OnboardingAdapter(onboardingItems) { position ->
            if (position < onboardingItems.size - 1) {
                viewPager.currentItem = position + 1
            } else {
                finishOnboarding()
            }
        }

        viewPager.adapter = adapter
        setupIndicators()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(position)
            }
        })

        tvSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(onboardingItems.size)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(this)
            indicators[i]?.setImageDrawable(
                getDrawable(R.drawable.indicator_inactive)
            )
            indicators[i]?.layoutParams = layoutParams
            indicatorLayout.addView(indicators[i])
        }
        updateIndicators(0)
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until indicatorLayout.childCount) {
            val indicator = indicatorLayout.getChildAt(i) as ImageView
            if (i == position) {
                indicator.setImageDrawable(getDrawable(R.drawable.indicator_active))
            } else {
                indicator.setImageDrawable(getDrawable(R.drawable.indicator_inactive))
            }
        }
    }

    private fun finishOnboarding() {
        // Save that user has completed onboarding
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit().putBoolean("onboarding_complete", true).apply()

        // Navigate to Sign In Activity (not MainActivity)
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

class OnboardingAdapter(
    private val items: List<OnboardingItem>,
    private val onNextClick: (Int) -> Unit
) : RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivOnboarding: ImageView = view.findViewById(R.id.ivOnboarding)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val btnNext: View = view.findViewById(R.id.btnNext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.ivOnboarding.setImageResource(item.imageRes)
        holder.tvTitle.text = item.title
        holder.tvDescription.text = item.description

        holder.btnNext.setOnClickListener {
            onNextClick(position)
        }
    }

    override fun getItemCount() = items.size
}