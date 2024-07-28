package com.example.mediforme.home.todayCondition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R

class WeekDayAdapter(private val items: List<WeekDayItem>) : RecyclerView.Adapter<WeekDayAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfWeek: TextView = itemView.findViewById(R.id.day_of_week)
        val date: TextView = itemView.findViewById(R.id.date)
        val emoji: ImageView = itemView.findViewById(R.id.emoji)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.dayOfWeek.text = item.dayOfWeek
        holder.date.text = item.date.toString()
        holder.emoji.setImageResource(item.emojiResId)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
