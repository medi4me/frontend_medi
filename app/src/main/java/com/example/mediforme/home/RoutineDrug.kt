package com.example.mediforme.home

data class RoutineDrug(
    val drugTime: String,
    val drugName: String,
    val drugNum: String,
    val drugCheckBtn: Int // 이미지 리소스 ID를 저장하는 Int 타입
)

