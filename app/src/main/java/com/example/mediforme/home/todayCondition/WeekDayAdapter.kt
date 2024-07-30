package com.example.mediforme.home.todayCondition

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R

class WeekDayAdapter(
    private val items: List<WeekDayItem>,
    private val onItemClick: (WeekDayItem) -> Unit
) : RecyclerView.Adapter<WeekDayAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfWeekTextView: TextView = itemView.findViewById(R.id.day_of_week)
        val dateTextView: TextView = itemView.findViewById(R.id.date_text)
        val imageView: ImageView = itemView.findViewById(R.id.emoji)
        val dayLinearLayout: LinearLayout = itemView.findViewById(R.id.day_LL)

        init {
            itemView.setOnClickListener {
                onItemClick(items[adapterPosition])
            }
        }

        fun bind(item: WeekDayItem) {
            dayOfWeekTextView.text = item.dayOfWeek
            dateTextView.text = item.date
            item.imageUri?.let {
                imageView.setImageURI(Uri.parse(it))
            } ?: run {
                imageView.setImageResource(R.drawable.ic_empty_emoji)
            }

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
