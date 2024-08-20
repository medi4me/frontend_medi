package com.example.mediforme.Data

import retrofit2.Call
import retrofit2.http.*

// TodayCondition 프레그먼트 캘린더

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


// 온보딩 약 검색

interface MedicineApi {
    @GET("/api/medi/itemName")
    fun searchMedicines(@Query("itemName") itemName: String): Call<MedicineResponse>
}

data class MedicineResponse(
    val medicines: List<Medicine>
)

data class Medicine(
    val itemName: String,
    val itemImage: String?
)