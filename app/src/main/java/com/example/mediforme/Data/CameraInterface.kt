package com.example.mediforme.Data

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CameraService {
    @Multipart
    @POST("/camera")
    fun uploadImage(
        @Part file: MultipartBody.Part
    ): Call<List<CameraMedicineResponse>> // 서버 응답에 맞게 데이터 클래스 정의 필요
}

data class CameraMedicineResponse(
    val name: String,
    val benefit: String,
    val drugInteraction: String?,
    val imageUrl: String?,
    val dosage: String,
    val alcoholWarning: String
)
