package com.example.mediforme.mypage

data class ContentDrug(
    val userMedicineId: Int,
    val contentDrugImg: Int,
    val contentDrugName: String,
    val contentDrugTime: String,
    val contentDrugFrequency: String,
    var isBellOn: Boolean // 알림 상태를 나타내는 boolean 값
)

