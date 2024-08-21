package com.example.mediforme.Data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// 온보딩 약 검색

interface MedicineApiService {
    @GET("api/medi/itemName")
    fun getMedicines(@Query("itemName") itemName: String): Call<MedicineResponse>
}

data class MedicineResponse(
    val medicines: List<Medicines>
)

data class Medicines(
    val itemName: String,
    val itemImage: String?
)
