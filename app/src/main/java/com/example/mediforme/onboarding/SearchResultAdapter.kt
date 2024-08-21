package com.example.mediforme.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediforme.Data.Medicines
import com.example.mediforme.R

class SearchResultAdapter(
    private var results: List<Medicines>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val medicineNumber: TextView = itemView.findViewById(R.id.medicine_number_tv)
        private val medicineName: TextView = itemView.findViewById(R.id.medicine_results)
        private val medicineImage: ImageView = itemView.findViewById(R.id.imageView5)

        fun bind(medicine: Medicines, position: Int) {
            medicineNumber.text = (position + 1).toString()
            medicineName.text = medicine.itemName
            Glide.with(itemView.context)
                .load(medicine.itemImage)
                .placeholder(R.drawable.ic_drug_default)
                .into(medicineImage)

            itemView.setOnClickListener {
                itemClickListener.onItemClick(medicine.itemName)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position], position)
    }

    override fun getItemCount() = results.size

    fun updateList(newList: List<Medicines>) {
        results = newList
        notifyDataSetChanged()
    }
}