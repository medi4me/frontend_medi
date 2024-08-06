package com.example.mediforme.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentAddMedicineBinding

class AddMedicineResultActivity : AppCompatActivity() {
    private lateinit var binding: FragmentAddMedicineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 데이터 받기
        val medicineName = intent.getStringExtra("medicine_name")
        val medicineDosage = intent.getStringExtra("medicine_dosage")

        // 데이터를 UI에 설정
        binding.medicineName.text = medicineName
        binding.medicineDosage.text = medicineDosage
    }
}


