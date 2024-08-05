package com.example.mediforme.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R

class SearchWithNameAdapter(private val results: List<searchResult>) : RecyclerView.Adapter<SearchWithNameAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicineNumber: TextView = itemView.findViewById(R.id.medicine_number_tv)
        val medicineName: TextView = itemView.findViewById(R.id.medicine_results)

        fun bind(result: searchResult, position: Int) {
            medicineNumber.text = (position + 1).toString() // 1부터 시작하도록 설정
            medicineName.text = result.name
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

data class searchResult(
    val imageResId: Int, // 이미지 리소스 ID
    val name: String
)
