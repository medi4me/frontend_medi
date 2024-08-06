package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.databinding.ActivitySplashChoiceBinding
import com.example.mediforme.login.LoginActivity
import com.example.mediforme.onboarding.OnboardingMedicineActivity


class SplashChoiceActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashChoiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.loginBtn.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.joinTV.setOnClickListener{
            startActivity(Intent(this, JoinServiceActivity::class.java))
        }

    }
}
