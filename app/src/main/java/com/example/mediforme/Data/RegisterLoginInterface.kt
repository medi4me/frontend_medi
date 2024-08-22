package com.example.mediforme.Data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Retrofit 인터페이스 정의
interface Register {
    @POST("register/name")
    fun registerUser(@Body userData: RegisterUserData): Call<RegisterResponse>
}

// 데이터 클래스 정의
data class RegisterUserData(
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("memberID") val memberID: String,
    @SerializedName("consent") val consent: String
)

data class RegisterResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ResultData?
)

data class ResultData(
    @SerializedName("memberID") val memberID: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)


//***********************************************************************

interface AuthService {
    //로그인
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    //아이디로 회원이름 값 받아오기
    @GET("auth/search-name/{memberID}")
    fun getName(@Path("memberID") memberID: String): Call<NameResponse>
}


data class LoginRequest(
    val memberID: String,
    val password: String
)

data class LoginResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
)

data class Result(
    val memberID: String,
    val accessToken: String,
    val refreshToken: String
)

//로그인 시 아이디 검색으로 이름 받아오기
data class NameResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)

//***************************************************************