package com.example.mediforme.remote.model.request

import com.google.gson.annotations.SerializedName

// --- 챗봇 관련 모델 ---
data class QuestionRequestDto(
    val question: String
)

// --- 온보딩 및 약물 관리 관련 모델 ---
data class MedicineRequest(
    @SerializedName("memberID") val memberID: String,
    @SerializedName("name") val name: String,
    @SerializedName("meal") val meal: String,
    @SerializedName("time") val time: String,
    @SerializedName("dosage") val dosage: String,
    @SerializedName("memberId") val memberId: Int
)

// --- 회원가입 및 로그인 관련 모델 ---
data class RegisterUserData(
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("memberID") val memberID: String,
    @SerializedName("consent") val consent: String
)

data class MemberIDRequest(
    val name: String = "string",
    val password: String = "string",
    val phone: String = "string",
    val memberID: String,
    val consent: String = "AGREE"
)

data class PhoneVerificationRequest(
    val phone: String,
    val verificationCode: String
)

data class LoginRequest(
    val memberID: String,
    val password: String
)

data class VerificationRequest(
    val phone: String,
    val verificationCode: String = "string"
)

// --- 상태 및 캘린더 관련 모델 ---
data class StatusRequest(
    @SerializedName("status") val status: String?,
    @SerializedName("drink") val drink: String?,
    @SerializedName("statusCondition") val statusCondition: String?,
    @SerializedName("memo") val memo: String?,
    @SerializedName("date") val date: String
)

data class CalenderUpdateRequest(
    @SerializedName("status") val status: String?,
    @SerializedName("drink") val drink: String?,
    @SerializedName("statusCondition") val statusCondition: String?,
    @SerializedName("memo") val memo: String?,
    @SerializedName("date") val date: String
)