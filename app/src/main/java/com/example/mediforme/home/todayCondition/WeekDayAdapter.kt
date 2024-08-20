package com.example.mediforme.home.todayCondition

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.Data.CalenderResponse
import com.example.mediforme.Data.CalenderStatus
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            if (item.isStatusLoaded) {
                // 상태가 이미 로드된 경우, 저장된 상태에 따라 이미지 설정
                when (item.status) {
                    "GOOD" -> imageView.setImageResource(R.drawable.ic_emoji_good)
                    "NOTBAD" -> imageView.setImageResource(R.drawable.ic_emoji_soso)
                    "BAD" -> imageView.setImageResource(R.drawable.ic_emoji_bad)
                    else -> imageView.setImageResource(R.drawable.ic_empty_emoji)
                }
            } else {
                // 기본 이미지로 초기화
                imageView.setImageResource(R.drawable.ic_empty_emoji)

                // 날짜 형식을 맞추기 (yyyy-MM-dd)
                val formattedDate = "2024-${item.month.toString().padStart(2, '0')}-${item.date.padStart(2, '0')}"

                // Retrofit을 사용하여 상태 데이터를 가져오기
                val calendarService = getRetrofit().create(CalenderStatus::class.java)
                calendarService.getDateDetails(formattedDate).enqueue(object : Callback<CalenderResponse> {
                    override fun onResponse(call: Call<CalenderResponse>, response: Response<CalenderResponse>) {
                        if (response.isSuccessful) {
                            val calenderResponse = response.body()
                            if (calenderResponse != null) {
                                // 상태 값 저장 및 이미지 설정
                                item.status = calenderResponse.status
                                item.isStatusLoaded = true

                                // 상태에 따른 이미지 설정
                                when (calenderResponse.status) {
                                    "GOOD" -> imageView.setImageResource(R.drawable.ic_emoji_good)
                                    "NOTBAD" -> imageView.setImageResource(R.drawable.ic_emoji_soso)
                                    "BAD" -> imageView.setImageResource(R.drawable.ic_emoji_bad)
                                    else -> imageView.setImageResource(R.drawable.ic_empty_emoji)
                                }
                            }
                        } else {
                            Log.e("WeekDayAdapter", "Failed to fetch data for date: $formattedDate, error code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<CalenderResponse>, t: Throwable) {
                        Log.e("WeekDayAdapter", "Error fetching data for date: $formattedDate", t)
                    }
                })
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
