package com.example.mediforme.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R

class SearchResultAdapter(private var results: List<SearchResult>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val medicineNumber: TextView = itemView.findViewById(R.id.medicine_number_tv)
        val medicineName: TextView = itemView.findViewById(R.id.medicine_results)
        val medicineImage: ImageView = itemView.findViewById(R.id.imageView5)

        fun bind(result: SearchResult, position: Int) {
            medicineNumber.text = (position + 1).toString()
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

    fun updateList(newList: List<SearchResult>) {
        results = newList
        notifyDataSetChanged()
    }
}

data class SearchResult(
    val imageResId: Int, // 이미지 리소스 ID
    val name: String
)
