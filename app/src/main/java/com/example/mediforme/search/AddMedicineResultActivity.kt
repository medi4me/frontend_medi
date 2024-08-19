package com.example.mediforme.search

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentAddMedicineBinding

class AddMedicineResultActivity : AppCompatActivity() {
    private lateinit var binding: FragmentAddMedicineBinding
    private var selectedTime: String? = null // 선택된 시간 저장 변수
    private var selectedMealTime: String? = null // 선택된 식사 시간 저장 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Intent로부터 데이터 받기
        val medicineName = intent.getStringExtra("medicine_name")
        val medicineDosage = intent.getStringExtra("medicine_dosage")

        // 데이터를 UI에 설정
        binding.medicineName.text = medicineName
        binding.medicineDosage.text = medicineDosage

        // 복용 시간 버튼 클릭 리스너 설정
        binding.doseTimeSpinner.setOnClickListener {
            showTimePickerDialog()
        }

        binding.backButton.setOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 프래그먼트로 돌아감
            onBackPressed()
        }

        binding.dosageOnetimeEV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val message = binding.dosageOnetimeEV.text.toString()
                binding.veriBtn.isEnabled = message.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 식사 시간 스피너에 아이템 선택 리스너 설정
        binding.mealTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedMealTime = parent.getItemAtPosition(position).toString() // 선택된 식사 시간 저장
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedMealTime = null
            }
        }

        binding.veriBtn.setOnClickListener {
            val dosageOnetime = binding.dosageOnetimeEV.text.toString()
            Log.d("AddMedicineResultActivity", "Selected Time: $selectedTime")
            Log.d("AddMedicineResultActivity", "Selected Meal Time: $selectedMealTime")
            Log.d("AddMedicineResultActivity", "Dosage One-time: $dosageOnetime")
            startActivity(Intent(this, AddMedicineActivity::class.java))
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
            selectedTime = String.format("%02d:%02d", hour, minute) // 선택된 시간을 변수에 저장
            binding.doseTimeSpinner.text = selectedTime
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
