package com.example.mediforme

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/verifyPhone")
    fun verifyPhone(@Body request: VerifyPhoneRequest): Call<VerifyPhoneResponse>
}

data class VerifyPhoneRequest(
    val phone: String,
    val verificationCode: String
)

data class VerifyPhoneResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Any?
)
