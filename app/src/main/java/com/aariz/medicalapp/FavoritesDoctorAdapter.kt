package com.aariz.medicalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class FavoriteDoctorAdapter(
    private val doctors: List<Doctor>
) : RecyclerView.Adapter<FavoriteDoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivDoctorImage: ImageView = view.findViewById(R.id.ivDoctorImage)
        val tvDoctorName: TextView = view.findViewById(R.id.tvDoctorName)
        val tvSpecialty: TextView = view.findViewById(R.id.tvSpecialty)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvReviews: TextView = view.findViewById(R.id.tvReviews)
        val ivFavorite: ImageView = view.findViewById(R.id.ivFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_card, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]

        holder.tvDoctorName.text = doctor.name
        holder.tvSpecialty.text = doctor.specialty
        holder.tvLocation.text = doctor.location

        val df = DecimalFormat("0.#")
        holder.tvRating.text = df.format(doctor.rating)

        holder.tvReviews.text = formatNumber(doctor.reviewCount) + " Reviews"
        holder.ivDoctorImage.setImageResource(doctor.imageResId)

        // Set favorite icon
        holder.ivFavorite.setImageResource(
            if (doctor.isFavorite) R.drawable.ic_heart_filled
            else R.drawable.ic_heart
        )

        holder.ivFavorite.setOnClickListener {
            doctor.isFavorite = !doctor.isFavorite
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            // Handle item click - navigate to doctor detail
        }
    }

    override fun getItemCount(): Int = doctors.size

    private fun formatNumber(num: Int): String {
        return when {
            num >= 1000 -> String.format("%,d", num)
            else -> num.toString()
        }
    }
}