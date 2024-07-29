package com.example.mediforme.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.databinding.ActivityOnboardingAgeBinding
import com.example.mediforme.search.SearchResultActivity

class OnboardingAgeActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingAgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.veriBtn.isEnabled = false

        binding.ageET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val message = binding.ageET.text.toString()
                binding.veriBtn.isEnabled = message.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.veriBtn.setOnClickListener {
            startActivity(Intent(this, OnboardingMedicineActivity::class.java))
        }
    }
}
