package com.example.mediforme.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R

class WeekDayAdapter2(
    private val items: List<WeekDayItem2>,
    private val onItemClick: (WeekDayItem2) -> Unit
) : RecyclerView.Adapter<WeekDayAdapter2.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfWeekTextView: TextView = itemView.findViewById(R.id.day_of_week)
        val dateTextView: TextView = itemView.findViewById(R.id.date_text)
        val dayLinearLayout: LinearLayout = itemView.findViewById(R.id.day_LL)

        init {
            itemView.setOnClickListener {
                onItemClick(items[adapterPosition])
            }
        }

        fun bind(item: WeekDayItem2) {
            dayOfWeekTextView.text = item.dayOfWeek
            dateTextView.text = item.date

            // Update selection state
            if (item.isSelected) {
                dateTextView.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                dayLinearLayout.isSelected = true
            } else {
                dateTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.calenderDate))
                dayLinearLayout.isSelected = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week_day2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
