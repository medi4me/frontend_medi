package com.example.mediforme.remote.model.response

import com.google.gson.annotations.SerializedName

// --- 공통 API 응답 구조 ---
data class ApiResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T
)

// --- 카메라 관련 모델 ---
data class CameraMedicineResponse(
    val name: String,
    val benefit: String,
    val drugInteraction: String?,
    val imageUrl: String?,
    val dosage: String,
    val alcoholWarning: String
)

data class MedicineInfoResponse(
    val name: String,
    val componentName: String,
    val amount: String
)

// --- 챗봇 관련 모델 ---
data class ChatGptResponseDto(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>
)

data class Choice(
    val message: MessageContent,
    val index: Int,
    val finish_reason: String
)

data class MessageContent(
    val role: String,
    val content: String
)

// --- 온보딩 및 약물 관리 관련 모델 ---
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
    @SerializedName("meal") val meal: String,
    @SerializedName("time") val time: String?,
    @SerializedName("dosage") val dosage: String?,
    @SerializedName("alarm") val alarm: Boolean,
    @SerializedName("check") val check: Boolean
)

// --- 회원가입 및 로그인 관련 모델 ---
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

data class MemberIDResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String
)

data class PhoneNumberResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)

data class PhoneVerificationResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
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

data class NameResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)

data class VerificationResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)

data class FindIDResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result2?
)

data class Result2(
    val memberID: String,
    val password: String
)

data class FindPasswordResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: PasswordResult?
)

data class PasswordResult(
    val memberID: String?,
    val password: String?
)

data class ResignResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)

data class LogoutResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)

// --- 상태 및 캘린더 관련 모델 ---
data class StatusResponse(
    @SerializedName("status") val status: String,
    @SerializedName("drink") val drink: String,
    @SerializedName("statusCondition") val statusCondition: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("date") val date: String
)

data class CalenderResponse(
    @SerializedName("status") val status: String,
    @SerializedName("drink") val drink: String,
    @SerializedName("statusCondition") val statusCondition: String,
    @SerializedName("memo") val memo: String,
    @SerializedName("date") val date: String
)