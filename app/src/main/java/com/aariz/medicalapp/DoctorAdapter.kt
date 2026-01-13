package com.aariz.medicalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class DoctorAdapter(
    private val doctorList: List<Doctor>,
    private val listener: OnDoctorClickListener
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    interface OnDoctorClickListener {
        fun onDoctorClick(doctor: Doctor)
        fun onFavoriteClick(doctor: Doctor, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_card, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]
        holder.bind(doctor)
    }

    override fun getItemCount(): Int = doctorList.size

    inner class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivDoctorImage: ImageView = itemView.findViewById(R.id.ivDoctorImage)
        private val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
        private val tvDoctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        private val tvSpecialty: TextView = itemView.findViewById(R.id.tvSpecialty)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        private val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        private val tvReviews: TextView = itemView.findViewById(R.id.tvReviews)

        fun bind(doctor: Doctor) {
            tvDoctorName.text = doctor.name
            tvSpecialty.text = doctor.specialty
            tvLocation.text = doctor.location

            val df = DecimalFormat("0.#")
            tvRating.text = df.format(doctor.rating)

            val reviewText = String.format("%,d Reviews", doctor.reviewCount)
            tvReviews.text = reviewText

            ivDoctorImage.setImageResource(doctor.imageResId)

            val heartIcon = if (doctor.isFavorite) {
                R.drawable.ic_heart_filled
            } else {
                R.drawable.ic_heart
            }
            ivFavorite.setImageResource(heartIcon)

            itemView.setOnClickListener {
                listener.onDoctorClick(doctor)
            }

            ivFavorite.setOnClickListener {
                doctor.isFavorite = !doctor.isFavorite
                listener.onFavoriteClick(doctor, adapterPosition)
                notifyItemChanged(adapterPosition)
            }
        }
    }
}