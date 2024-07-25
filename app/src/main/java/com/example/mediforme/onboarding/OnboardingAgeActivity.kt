package com.example.mediforme.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.databinding.ActivityOnboardingAgeBinding

class OnboardingAgeActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingAgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
