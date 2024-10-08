package com.example.mediforme.Data

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// 온보딩 약 검색
interface MedicineApiService {
    @GET("api/medi/itemName")
    fun getMedicines(@Query("name") itemName: String
    ): Call<MedicineResponse>
}


// 온보딩 약 추가
interface MedicineSaveService {
    @POST("api/medi/save")
    fun saveMedicine(
        @Query("memberID") memberID: String,
        @Query("name") name: String,
        @Query("meal") meal: String,
        @Query("time") time: String,
        @Query("dosage") dosage: String,
        @Query("memberId") memberId: Int
    ): Call<MedicineResponse>
}


// 온보딩, 메인화면, 마이페이지에서 복용약 모두 조회
interface MedicineShowService {
    @GET("list/medicines")
    fun getUserMedicines(
        @Header("Authorization") token: String
    ): Call<MedicineResponse>
}



// 마이페이지 복용약 삭제
interface MedicineDeleteService {
    @DELETE("delete/userMedicine")
    fun deleteMedicine(
        @Header("Authorization") token: String,       // Authorization 헤더 추가
        @Query("userMedicineId") userMedicineId: Int  // Query parameter로 userMedicineId만 사용
    ): Call<ResponseBody>
}



// 메인화면 checkbox 체크, 해체 api
//interface MedicineCheckService {
//    @PUT("{userMedicineId}/check")
//    fun checkMedicine(
//        @Path("userMedicineId") userMedicineId: Int
//    ): Call<Void>
//}
// 메인화면 checkbox 체크, 해체 api
interface MedicineCheckService {
    @PUT("{userMedicineId}/check")
    fun checkMedicine(
        @Header("Authorization") token: String,
        @Path("userMedicineId") userMedicineId: Int
    ): Call<Void>
}

interface MedicineCheckOffService {
    @PUT("{userMedicineId}/checkOff")
    fun uncheckMedicine(
        @Header("Authorization") token: String,
        @Path("userMedicineId") userMedicineId: Int
    ): Call<Void>
}

// 마이페이지 알람 체크, 해체 api
interface MedicineAlarmService {
    @PUT("{userMedicineId}/check/alarm")
    fun checkMedicineAlarm(
        @Header("Authorization") token: String,
        @Path("userMedicineId") userMedicineId: Int
    ): Call<Void>
}

interface MedicineAlarmOffService {
    @PUT("{userMedicineId}/check/alarmOff")
    fun uncheckMedicineAlarm(
        @Header("Authorization") token: String,
        @Path("userMedicineId") userMedicineId: Int
    ): Call<Void>
}




data class MedicineResponse(
    val medicines: List<Medicines>
)

data class Medicines(
    @SerializedName("userMedicineId") val userMedicineId: Int,
    @SerializedName("itemName") val itemName: String,
    @SerializedName("itemImage") val itemImage: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("benefit") val benefit: String?,
    @SerializedName("drugInteraction") val drugInteraction: String?,
    @SerializedName("meal") val meal: String,
    @SerializedName("time") val time: String?,
    @SerializedName("dosage") val dosage: String?,
    @SerializedName("alarm") val alarm: Boolean,
    @SerializedName("check") val check: Boolean
)

data class MedicineRequest(
    @SerializedName("memberID") val memberID: String,
    @SerializedName("name") val name: String,
    @SerializedName("meal") val meal: String,
    @SerializedName("time") val time: String,
    @SerializedName("dosage") val dosage: String,
    @SerializedName("memberId") val memberId: Int
)