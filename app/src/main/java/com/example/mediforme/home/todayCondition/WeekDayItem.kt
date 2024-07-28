package com.example.mediforme.home.todayCondition

import java.text.SimpleDateFormat
import java.util.*

data class WeekDayItem(val dayOfWeek: String, val date: String, val month: Int, var imageUri: String? = null, var isSelected: Boolean = false)

fun getWeekDates(): List<WeekDayItem> {
    val dayOfWeekFormat = SimpleDateFormat("E", Locale("ko"))
    val dateFormat = SimpleDateFormat("d", Locale.getDefault())
    val monthFormat = SimpleDateFormat("M", Locale.getDefault())
    val calendar = Calendar.getInstance()
    val dates = mutableListOf<WeekDayItem>()

    for (i in 0 until 40) {
        dates.add(WeekDayItem(
            dayOfWeek = dayOfWeekFormat.format(calendar.time),
            date = dateFormat.format(calendar.time),
            month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 +1
        ))
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return dates
}
