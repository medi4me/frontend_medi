package com.example.mediforme.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediforme.Data.Medicines
import com.example.mediforme.R

class SearchWithNameAdapter(private val results: List<Medicines>) : RecyclerView.Adapter<SearchWithNameAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val medicineNumber: TextView = itemView.findViewById(R.id.medicine_number_tv)
        private val medicineName: TextView = itemView.findViewById(R.id.medicine_results)
        private val medicineImage: ImageView = itemView.findViewById(R.id.imageView5)

        fun bind(result: Medicines, position: Int) {
            medicineNumber.text = (position + 1).toString() // 1부터 시작하도록 설정
            medicineName.text = result.itemName

            if (result.itemImage != null) {
                Glide.with(itemView.context)
                    .load(result.itemImage)
                    .into(medicineImage)
            } else {
                medicineImage.setImageResource(R.drawable.ic_drug_default) // 기본 이미지 리소스
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position], position)
    }

    override fun getItemCount() = results.size
}
