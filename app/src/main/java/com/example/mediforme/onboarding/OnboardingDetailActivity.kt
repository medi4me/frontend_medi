package com.example.mediforme.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.databinding.ActivityOnboardingDetailBinding

class OnboardingDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 식사 시간 스피너에 아이템 선택 리스너를 설정
        binding.mealTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // 아이템이 선택되었을 때 호출되는 메소드
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@OnboardingDetailActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            // 아무 아이템도 선택되지 않았을 때 호출되는 메소드
            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택되지 않았을 때의 동작 (여기서는 아무것도 하지 않음)
            }
        }

        binding.veriBtn.isEnabled = false

        binding.dosageOnetimeEV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val message = binding.dosageOnetimeEV.text.toString()
                binding.veriBtn.isEnabled = message.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.veriBtn.setOnClickListener {
            startActivity(Intent(this, OnboardingMedicineActivity::class.java))
        }
    }
}
