package com.example.mediforme.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R

class SearchWithNameAdapter(private val results: List<SearchResult>) : RecyclerView.Adapter<SearchWithNameAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val medicineNumber: TextView = itemView.findViewById(R.id.medicine_number_tv)
        private val medicineName: TextView = itemView.findViewById(R.id.medicine_results)

        fun bind(result: SearchResult, position: Int) {
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
