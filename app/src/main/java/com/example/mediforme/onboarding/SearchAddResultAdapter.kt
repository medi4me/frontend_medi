package com.example.mediforme.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R

class SearchAddResultAdapter(
    private val results: List<SearchAddResult>,
) : RecyclerView.Adapter<SearchAddResultAdapter.SearchAddResultViewHolder>() {


    inner class SearchAddResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.medicine_result_name)
        val contentsTextView: TextView = itemView.findViewById(R.id.medicine_contents)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAddResultViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_add_result, parent, false)
        return SearchAddResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchAddResultViewHolder, position: Int) {
        val currentItem = results[position]
        holder.nameTextView.text = currentItem.name
        holder.contentsTextView.text = currentItem.contents
    }

    override fun getItemCount() = results.size
}

data class SearchAddResult(
    val name: String,
    val contents: String
)
