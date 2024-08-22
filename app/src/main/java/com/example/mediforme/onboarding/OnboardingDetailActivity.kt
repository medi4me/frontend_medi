package com.example.mediforme.onboarding

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
import com.example.mediforme.Data.MedicineApiService
import com.example.mediforme.Data.MedicineRequest
import com.example.mediforme.Data.MedicineResponse
import com.example.mediforme.Data.Medicines
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.databinding.ActivityOnboardingDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnboardingDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingDetailBinding
    private var selectedTime: String? = null // 선택된 시간 저장 변수
    private var selectedMealTime: String? = null // 선택된 식사 시간 저장 변수
    private val memberId: String = "1" // 고정된 멤버 ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

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

        // 식사 시간 스피너에 아이템 선택 리스너 설정
        binding.mealTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedMealTime = parent.getItemAtPosition(position).toString() // 선택된 식사 시간 저장
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedMealTime = null
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
            val dosageOnetime = binding.dosageOnetimeEV.text.toString()
            // 선택된 시간, 식사 시간, 복용량을 로그로 출력
            Log.d("OnboardingDetailActivity", "Medicine Name: $medicineName")
            Log.d("OnboardingDetailActivity", "Selected Time: $selectedTime")
            Log.d("OnboardingDetailActivity", "Selected Meal Time: $selectedMealTime")
            Log.d("OnboardingDetailActivity", "Dosage One-time: $dosageOnetime")

            // API 호출하여 데이터 전송
            saveMedicineToServer("타이레놀", "MEAL", selectedTime, dosageOnetime,memberId)

            //startActivity(Intent(this, OnboardingMedicineActivity::class.java))
        }

        binding.skippingTv.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun saveMedicineToServer(name: String?, meal: String?, time: String?, dosage: String?, memberId: String) {
        if (name == null || meal == null || time == null || dosage == null) {
            Log.e("OnboardingDetailActivity", "Missing required fields")
            return
        }

        // Retrofit 설정 (기존 getRetrofit() 함수 사용)
        val retrofit = getRetrofit()
        val apiService = retrofit.create(MedicineApiService::class.java)

        // MedicineRequest 객체 생성
        val request = MedicineRequest(
            name = name,
            meal = meal,
            time = time,
            dosage = dosage,
            memberId = memberId
        )

        // 요청 본문 로그 출력
        Log.d("OnboardingDetailActivity", "Sending request with body: $request")

        // API 호출
        val call = apiService.saveMedicine(request)
        call.enqueue(object : Callback<MedicineResponse> {
            override fun onResponse(call: Call<MedicineResponse>, response: Response<MedicineResponse>) {
                if (response.isSuccessful) {
                    Log.d("OnboardingDetailActivity", "Medicine saved successfully")
                    startActivity(Intent(this@OnboardingDetailActivity, OnboardingMedicineActivity::class.java))
                } else {
                    // 실패 시 응답 본문 로그 출력
                    val errorBody = response.errorBody()?.string()
                    Log.e("OnboardingDetailActivity", "Failed to save medicine. HTTP Status: ${response.code()}. Error: $errorBody")
                }
            }

            override fun onFailure(call: Call<MedicineResponse>, t: Throwable) {
                Log.e("OnboardingDetailActivity", "Error saving medicine", t)
            }
        })
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
