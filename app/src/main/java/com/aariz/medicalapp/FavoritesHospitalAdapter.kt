package com.aariz.medicalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class FavoriteHospitalAdapter(
    private val hospitals: List<Hospital>
) : RecyclerView.Adapter<FavoriteHospitalAdapter.HospitalViewHolder>() {

    inner class HospitalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivHospitalImage: ImageView = view.findViewById(R.id.ivHospitalImage)
        val tvHospitalName: TextView = view.findViewById(R.id.tvHospitalName)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvReviews: TextView = view.findViewById(R.id.tvReviews)
        val tvDistance: TextView = view.findViewById(R.id.tvDistance)
        val tvType: TextView = view.findViewById(R.id.tvType)
        val btnFavorite: View = view.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hospital, parent, false)
        return HospitalViewHolder(view)
    }

    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        val hospital = hospitals[position]

        holder.tvHospitalName.text = hospital.name
        holder.tvAddress.text = hospital.address

        val df = DecimalFormat("0.#")
        holder.tvRating.text = df.format(hospital.rating)

        holder.tvReviews.text = "(${hospital.reviews} Reviews)"
        holder.tvDistance.text = hospital.distance
        holder.tvType.text = hospital.type
        holder.ivHospitalImage.setImageResource(hospital.imageRes)

        holder.btnFavorite.setOnClickListener {
            hospital.isFavorite = !hospital.isFavorite
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            // Handle item click - navigate to hospital detail
        }
    }

    override fun getItemCount(): Int = hospitals.size
}