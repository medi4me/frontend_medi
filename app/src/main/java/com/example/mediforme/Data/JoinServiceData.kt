package com.example.mediforme.Data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register/consent")
    fun postConsent(@Body request: ConsentRequest): Call<ServerResponse>
}

data class ConsentRequest(
    val name: String,
    val password: String,
    val phone: String,
    val memberID: String,
    val consent: String = "AGREE"
)

data class ServerResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Any?
)
