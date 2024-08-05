package com.example.mediforme.onboarding

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivityOnboardingDetailBinding

class OnboardingDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val medicineName = intent.getStringExtra("medicine_name")
        if (medicineName != null) {
            binding.medicineNameTv.text = medicineName
        }

        // 복용 시간 버튼 클릭 리스너 설정
        binding.doseTimeSpinner.setOnClickListener {
            showTimePickerDialog()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // 식사 시간 스피너에 아이템 선택 리스너를 설정
        binding.mealTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@OnboardingDetailActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

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

        binding.skippingTv.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun showTimePickerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_time_picker, null)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.btnOk).setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            binding.doseTimeSpinner.text = String.format("%02d:%02d", hour, minute)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
