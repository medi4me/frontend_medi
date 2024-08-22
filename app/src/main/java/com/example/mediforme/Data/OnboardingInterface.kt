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
    fun getMedicines(@Query("name") itemName: String
    ): Call<MedicineResponse>
}

interface MedicineSaveService {
    @POST("api/medi/save")
    fun saveMedicine(@Body medicineRequest: MedicineRequest
    ): Call<MedicineResponse>
}

interface MedicineShowService{
    @GET("list/medicines")
    fun getUserMedicines(@Query("memberId") memberId: String
    ): Call<MedicineResponse>
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
    @SerializedName("meal") val meal: String?,
    @SerializedName("time") val time: String?,
    @SerializedName("dosage") val dosage: String?,
    @SerializedName("check") val check: Boolean,
    @SerializedName("alarm") val alarm: Boolean
)

data class MedicineRequest(
    @SerializedName("name") val name: String,
    @SerializedName("meal") val meal: String,
    @SerializedName("time") val time: String,
    @SerializedName("dosage") val dosage: String,
    @SerializedName("memberId") val memberId: String
)