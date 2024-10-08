package com.example.mediforme.Data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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

    @POST("register/verifyPhone")
    fun verifyPhone(@Body request: PhoneVerificationRequest): Call<PhoneVerificationResponse>
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

//인증번호 인증 확인 request
data class PhoneVerificationRequest(
    val phone: String,
    val verificationCode: String
)

//인증번호 전송 확인 response
data class PhoneVerificationResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)


//로그인
//***********************************************************************

interface AuthService {
    //로그인
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // 회원 탈퇴
    @POST("resign")
    fun resign(@Header("Authorization") token: String): Call<ResignResponse>

    //아이디로 회원이름 값 받아오기
    @GET("auth/search-name/{memberID}")
    fun getName(@Path("memberID") memberID: String): Call<NameResponse>

    //아이디 찾기, 비밀번호 찾기 인증번호 전송
    @POST("find/send-verification-code")
    fun sendVerificationCode(@Body request: VerificationRequest): Call<VerificationResponse>

    //아이디 찾기 인증번호 일치 확인
    @POST("find/verify-and-find-id")
    fun verifyAndFindID(@Body request: VerificationRequest): Call<FindIDResponse>

    //비밀번호 찾기 인증번호 일치 확인
    @POST("/find/verify-and-find-password")
    fun verifyAndFindPassword(@Body request: VerificationRequest): Call<FindPasswordResponse>

    // 로그아웃
    @POST("auth/logout")
    fun logout(@Header("Authorization") token: String): Call<LogoutResponse>

}

//회원 탈퇴 response
data class ResignResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)

//로그아웃 response
data class LogoutResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)

// 로그인 request
data class LoginRequest(
    val memberID: String,
    val password: String
)

//로그인 response
data class LoginResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
)
//로그인 response
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

//아이디 찾기, 비밀번호 찾기 인증번호 전송 request
data class VerificationRequest(
    val phone: String,
    val verificationCode: String = "string"  // 기본값 설정
)

//아이디 찾기, 비밀번호 찾기 인증번호 전송 response
data class VerificationResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)


//아이디 찾기 인증번호 일치 확인 response
data class FindIDResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result2?
)

//아이디 찾기 인증번호 일치 확인 request
data class Result2(
    val memberID: String,
    val password: String
)


//비밀번호 찾기 인증번호 일치 확인 response
data class FindPasswordResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: PasswordResult?
)
//비밀번호 찾기 인증번호 일치 확인 response
data class PasswordResult(
    val memberID: String?,
    val password: String?
)

//***************************************************************