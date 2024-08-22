package com.example.mediforme.Data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// 온보딩 약 검색

interface MedicineApiService {
    @GET("api/medi/itemName")
    fun getMedicines(@Query("name") itemName: String): Call<MedicineResponse>

    @POST("api/medi/save")
    fun saveMedicine(@Body medicineRequest: MedicineRequest): Call<MedicineResponse>
}

data class MedicineResponse(
    val medicines: List<Medicines>
)

data class Medicines(
    val itemName: String,
    val itemImage: String?,
    val description: String?,
    val benefit: String?,
    val drugInteraction: String?,
    val meal: String?,
    val time: String?,
    val dosage: String?
)

data class MedicineRequest(
    @SerializedName("name") val name: String,
    @SerializedName("meal") val meal: String,
    @SerializedName("time") val time: String,
    @SerializedName("dosage") val dosage: String,
    @SerializedName("memberId") val memberId: String
)