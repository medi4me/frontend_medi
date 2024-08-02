package com.example.mediforme.home

import java.text.SimpleDateFormat
import java.util.*

data class WeekDayItem2(
    val dayOfWeek: String,
    val date: String,
    val month: Int,
    var isSelected: Boolean = false
)

fun getWeekDates(): Pair<List<WeekDayItem2>, Int> {
    val dayOfWeekFormat = SimpleDateFormat("E", Locale("ko"))
    val dateFormat = SimpleDateFormat("d", Locale.getDefault())
    val monthFormat = SimpleDateFormat("M", Locale.getDefault())
    val calendar = Calendar.getInstance()
    val dates = mutableListOf<WeekDayItem2>()

    // 이전 21일 (3주)
    calendar.add(Calendar.DAY_OF_MONTH, -21)
    for (i in 0 until 42) { // 42일 (6주)
        dates.add(WeekDayItem2(
            dayOfWeek = dayOfWeekFormat.format(calendar.time),
            date = dateFormat.format(calendar.time),
            month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 +1
        ))
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    // 현재 날짜가 위치한 인덱스를 반환
    val todayIndex = 21 // 오늘 날짜는 리스트의 중간에 위치
    return Pair(dates, todayIndex)
}
