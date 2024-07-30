package com.example.mediforme.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mediforme.databinding.FragmentAddMedicineBinding

class AddMedicineFragment(private val medicine: Medicine) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddMedicineBinding.inflate(inflater, container, false)
        // 선택된 약의 정보를 설정합니다.
        binding.medicineName.text = medicine.name
        binding.medicineDosage.text = medicine.dosage
        return binding.root
    }
}
