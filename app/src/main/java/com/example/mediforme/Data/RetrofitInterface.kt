package com.example.mediforme.Data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

//**************************************************************************//

interface Status {
    @POST("status")
    fun addStatus(
        @Body statusRequest: StatusRequest
    ): Call<StatusResponse>
}

//상태조회 요청
data class StatusRequest(
    @SerializedName("status") val status: String,
    @SerializedName("drink") val drink: String,
    @SerializedName("statusCondition") val statusCondition: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("date") val date: String
)
//상태 조회 응답
data class StatusResponse(
    @SerializedName("status") val status: String,
    @SerializedName("drink") val drink: String,
    @SerializedName("statusCondition") val statusCondition: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("date") val date: String
)

//**************************************************************************//

interface CalendarApi {
    @GET("calender/date/{date}")
    fun getDateDetails(
        @Path("date") date: String
    ): Call<CalendarResponse>
}

//날짜 선택 응답
data class CalendarResponse(
    @SerializedName("status") val status: String,
    @SerializedName("drink") val drink: String,
    @SerializedName("statusCondition") val statusCondition: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("date") val date: String
)

//**************************************************************************//

// 온보딩 약 검색

interface MedicineApi {
    @GET("api/medi/itemName")
    fun searchMedicines(@Query("itemName") itemName: String): Call<MedicineResponse>
}

data class MedicineResponse(
    val medicines: List<Medicine>
)

data class Medicine(
    val itemName: String,
    val itemImage: String?
)