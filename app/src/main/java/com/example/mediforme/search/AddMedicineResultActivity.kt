package com.example.mediforme.search

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentAddMedicineBinding

class AddMedicineResultActivity : AppCompatActivity() {
    private lateinit var binding: FragmentAddMedicineBinding

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


