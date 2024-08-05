package com.example.mediforme.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R

class SearchResultAdapter(private val results: List<searchResults>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicineNumber: TextView = itemView.findViewById(R.id.medicine_number_tv)
        val medicineName: TextView = itemView.findViewById(R.id.medicine_results)
        val medicineImage: ImageView = itemView.findViewById(R.id.imageView5) // assuming imageView5 is the ID for the image

        fun bind(result: searchResults, position: Int) {
            medicineNumber.text = (position + 1).toString() // 1부터 시작하도록 설정
            medicineName.text = result.name
            medicineImage.setImageResource(result.imageResId)
            itemView.setOnClickListener {
                itemClickListener.onItemClick(result.name)
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
}

data class searchResults(
    val imageResId: Int, // 이미지 리소스 ID
    val name: String
)
