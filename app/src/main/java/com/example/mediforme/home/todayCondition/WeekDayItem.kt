package com.example.mediforme.home.todayCondition

import java.text.SimpleDateFormat
import java.util.*

data class WeekDayItem(val dayOfWeek: String, val date: String, var imageUri: String? = null, var isSelected: Boolean = false)

fun getWeekDates(): List<WeekDayItem> {
    val dayOfWeekFormat = SimpleDateFormat("E", Locale("ko"))
    val dateFormat = SimpleDateFormat("d", Locale.getDefault())
    val calendar = Calendar.getInstance()
    val dates = mutableListOf<WeekDayItem>()

    for (i in 0 until 40) {
        dates.add(WeekDayItem(dayOfWeekFormat.format(calendar.time), dateFormat.format(calendar.time)))
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return dates
}
