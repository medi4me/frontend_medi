package com.example.mediforme.Data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

//**************************************************************************//

interface Status {
    @POST("api/status")
    fun addStatus(
        @Body statusRequest: StatusRequest
    ): Call<StatusResponse>
}

//상태조회 요청(저장), //null값 허용
data class StatusRequest(
    @SerializedName("status") val status: String?,
    @SerializedName("drink") val drink: String?,
    @SerializedName("statusCondition") val statusCondition: String?,
    @SerializedName("memo") val memo: String?,
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

interface CalenderStatus {
    @GET("api/status/date/{date}")
    fun getDateDetails(
        @Path("date") date: String
    ): Call<CalenderResponse>

    @PUT("api/status/date/{date}")
    fun updateDateStatus(
        @Path("date") date: String,
        @Body updateRequest: CalenderUpdateRequest
    ): Call<CalenderResponse>
}

//날짜 선택 응답
data class CalenderResponse(
    @SerializedName("status") val status: String,
    @SerializedName("drink") val drink: String,
    @SerializedName("statusCondition") val statusCondition: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("date") val date: String
)

//날짜 수정 요청
data class CalenderUpdateRequest(
    @SerializedName("status") val status: String?,
    @SerializedName("drink") val drink: String?,
    @SerializedName("statusCondition") val statusCondition: String?,
    @SerializedName("memo") val memo: String?,
    @SerializedName("date") val date: String
)

//**************************************************************************//

