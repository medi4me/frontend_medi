package com.example.mediforme.Data

import retrofit2.Call
import retrofit2.http.*

interface CalendarApiService {

    @GET("status/date/{date}")
    fun getStatusByDate(@Path("date") date: String): Call<Status>

    @POST("calender")
    fun addCalendarEntry(@Body calendarEntry: CalendarEntry): Call<CalendarEntry>
}

data class Status(
    val status: String,
    val drink: String,
    val statusCondition: String,
    val memo: String,
    val date: String
)

data class CalendarEntry(
    val id: Long,
    val date: String,
    val memberId: Int,
    val statusId: Int
)
