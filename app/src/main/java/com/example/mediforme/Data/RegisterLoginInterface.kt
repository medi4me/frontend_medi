package com.example.mediforme.Data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

//회원가입
//***********************************************************************
interface Register {
    @POST("register/name")
    fun registerUser(@Body userData: RegisterUserData): Call<RegisterResponse>

    @POST("register/memberID")
    fun checkMemberID(@Body request: MemberIDRequest): Call<MemberIDResponse>

    @POST("register/phone")
    fun checkPhoneNumber(@Query("phone") phone: String): Call<PhoneNumberResponse>

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

//아이디 중복검사 request
data class MemberIDRequest(
    val name: String = "string",  // 기본값 설정
    val password: String = "string",  // 기본값 설정 (비밀번호는 여기서 사용하지 않으므로 설정 필요 없음)
    val phone: String = "string",  // 기본값 설정
    val memberID: String,  // 사용자가 입력한 ID를 이 필드에 넣음
    val consent: String = "AGREE"  // 기본값 설정
)

//아이디 중복검사 response
data class MemberIDResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String
)

//전화번호 중복검사,인증번호 sms전송
data class PhoneNumberResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)


//로그인
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