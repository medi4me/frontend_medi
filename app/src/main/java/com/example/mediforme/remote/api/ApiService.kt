package com.example.mediforme.remote.api

import com.example.mediforme.remote.model.request.CalenderUpdateRequest
import com.example.mediforme.remote.model.request.LoginRequest
import com.example.mediforme.remote.model.request.MemberIDRequest
import com.example.mediforme.remote.model.request.PhoneVerificationRequest
import com.example.mediforme.remote.model.request.QuestionRequestDto
import com.example.mediforme.remote.model.request.RegisterUserData
import com.example.mediforme.remote.model.request.StatusRequest
import com.example.mediforme.remote.model.request.VerificationRequest
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.CalenderResponse
import com.example.mediforme.remote.model.response.CameraMedicineResponse
import com.example.mediforme.remote.model.response.ChatGptResponseDto
import com.example.mediforme.remote.model.response.FindIDResponse
import com.example.mediforme.remote.model.response.FindPasswordResponse
import com.example.mediforme.remote.model.response.LoginResponse
import com.example.mediforme.remote.model.response.LogoutResponse
import com.example.mediforme.remote.model.response.MedicineInfoResponse
import com.example.mediforme.remote.model.response.MedicineResponse
import com.example.mediforme.remote.model.response.MemberIDResponse
import com.example.mediforme.remote.model.response.NameResponse
import com.example.mediforme.remote.model.response.PhoneNumberResponse
import com.example.mediforme.remote.model.response.PhoneVerificationResponse
import com.example.mediforme.remote.model.response.RegisterResponse
import com.example.mediforme.remote.model.response.ResignResponse
import com.example.mediforme.remote.model.response.StatusResponse
import com.example.mediforme.remote.model.response.VerificationResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

// 모든 API 서비스 인터페이스를 하나로 통합합니다.
interface ApiService {

    // --- 회원가입 및 로그인 관련 API ---
    @POST("register/name")
    suspend fun registerUser(@Body userData: RegisterUserData): Response<ApiResponse<RegisterResponse>>

    @POST("register/memberID")
    suspend fun checkMemberID(@Body request: MemberIDRequest): Response<ApiResponse<MemberIDResponse>>

    @POST("register/phone")
    suspend fun checkPhoneNumber(@Query("phone") phone: String): Response<ApiResponse<PhoneNumberResponse>>

    @POST("register/verifyPhone")
    suspend fun verifyPhone(@Body request: PhoneVerificationRequest): Response<ApiResponse<PhoneVerificationResponse>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("resign")
    suspend fun resign(@Header("Authorization") token: String): Response<ApiResponse<ResignResponse>>

    @GET("auth/search-name/{memberID}")
    suspend fun getName(@Path("memberID") memberID: String): Response<ApiResponse<NameResponse>>

    @POST("find/send-verification-code")
    suspend fun sendVerificationCode(@Body request: VerificationRequest): Response<ApiResponse<VerificationResponse>>

    @POST("find/verify-and-find-id")
    suspend fun verifyAndFindID(@Body request: VerificationRequest): Response<ApiResponse<FindIDResponse>>

    @POST("/find/verify-and-find-password")
    suspend fun verifyAndFindPassword(@Body request: VerificationRequest): Response<ApiResponse<FindPasswordResponse>>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse<LogoutResponse>>


    // --- 온보딩 및 약물 관리 관련 API ---
    @GET("api/medi/itemName")
    suspend fun getMedicines(@Query("name") itemName: String): Response<ApiResponse<MedicineResponse>>

    @POST("api/medi/save")
    suspend fun saveMedicine(
        @Query("memberID") memberID: String,
        @Query("name") name: String,
        @Query("meal") meal: String,
        @Query("time") time: String,
        @Query("dosage") dosage: String,
        @Query("memberId") memberId: Int
    ): Response<ApiResponse<MedicineResponse>>

    @GET("list/medicines")
    suspend fun getUserMedicines(@Header("Authorization") token: String): Response<ApiResponse<MedicineResponse>>

    @DELETE("delete/userMedicine")
    suspend fun deleteMedicine(
        @Header("Authorization") token: String,
        @Query("userMedicineId") userMedicineId: Int
    ): Response<ResponseBody> // ResponseBody는 HTTP 응답 본문 자체를 받으므로, API 응답 형식을 따르지 않을 때 사용합니다.

    @PUT("{userMedicineId}/check")
    suspend fun checkMedicine(
        @Header("Authorization") token: String,
        @Path("userMedicineId") userMedicineId: Int
    ): Response<Void> // 서버에서 특정 응답 본문 없이 성공만 반환할 때 Void를 사용합니다.

    @PUT("{userMedicineId}/checkOff")
    suspend fun uncheckMedicine(
        @Header("Authorization") token: String,
        @Path("userMedicineId") userMedicineId: Int
    ): Response<Void>

    @PUT("{userMedicineId}/check/alarm")
    suspend fun checkMedicineAlarm(
        @Header("Authorization") token: String,
        @Path("userMedicineId") userMedicineId: Int
    ): Response<Void>

    @PUT("{userMedicineId}/check/alarmOff")
    suspend fun uncheckMedicineAlarm(
        @Header("Authorization") token: String,
        @Path("userMedicineId") userMedicineId: Int
    ): Response<Void>


    // --- 카메라 및 약물 세부 정보 관련 API ---
    @Multipart
    @POST("/camera")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<ApiResponse<List<CameraMedicineResponse>>> // List<CameraMedicineResponse>를 ApiResponse로 감쌉니다.

    @GET("/medicine-ingredient")
    suspend fun getMedicineInfo(
        @Query("name") names: List<String>
    ): Response<ApiResponse<List<MedicineInfoResponse>>> // List<MedicineInfoResponse>를 ApiResponse로 감쌉니다.


    // --- 상태 및 캘린더 관련 API ---
    @POST("api/status")
    suspend fun addStatus(
        @Body statusRequest: StatusRequest
    ): Response<ApiResponse<StatusResponse>>

    @GET("api/status/date/{date}")
    suspend fun getDateDetails(
        @Path("date") date: String
    ): Response<ApiResponse<CalenderResponse>>

    @PUT("api/status/date/{date}")
    suspend fun updateDateStatus(
        @Path("date") date: String,
        @Body updateRequest: CalenderUpdateRequest
    ): Response<ApiResponse<CalenderResponse>>

    // --- 챗봇 관련 API ---
    @POST("/chat-gpt/question")
    suspend fun askQuestion(
        @Header("Authorization") authToken: String,
        @Body questionRequest: QuestionRequestDto
    ): Response<ApiResponse<ChatGptResponseDto>>
}
