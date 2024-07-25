package com.example.mediforme.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.databinding.ActivityOnboardingMedicineBinding

class OnboardingMedicineActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingMedicineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
