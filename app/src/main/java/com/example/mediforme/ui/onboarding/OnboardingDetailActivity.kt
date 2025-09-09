package com.example.mediforme.ui.onboarding

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.lifecycle.lifecycleScope
import com.example.mediforme.ui.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivityOnboardingDetailBinding
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.request.MedicineRequest
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.MedicineResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class OnboardingDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingDetailBinding
    private var selectedTime: String? = null // 선택된 시간 저장 변수
    private var selectedMealTime: String? = null // 선택된 식사 시간 저장 변수
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var memberID: String

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // SharedPreferences에서 memberID 가져오기
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        memberID = sharedPreferences.getString("memberID", null) ?: ""
        val memberIdInt = sharedPreferences.getString("memberId", "0")?.toIntOrNull() ?: 0

        if (memberID.isEmpty()) {
            Log.e("OnboardingDetailActivity", "Member ID is missing in SharedPreferences")
            // 필요한 경우, memberID가 없는 경우에 대한 처리 로직 추가
        }

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

            // selectedMealTime 값을 변환
            val mealTime = when (selectedMealTime) {
                "식전" -> "NOMEAL"
                "식후" -> "MEAL"
                else -> selectedMealTime // 만약 다른 값이 있으면 원래 값을 그대로 사용
            }

            val medicineSaveName = binding.medicineNameTv.text.toString()

            // 선택된 시간, 식사 시간, 복용량을 로그로 출력
            Log.d("OnboardingDetailActivity", "Medicine Name: $medicineSaveName")
            Log.d("OnboardingDetailActivity", "Selected Time: $selectedTime")
            Log.d("OnboardingDetailActivity", "Selected Meal Time: $mealTime")
            Log.d("OnboardingDetailActivity", "Dosage One-time: $dosageOnetime")

            // MedicineRequest 데이터 클래스에 필요한 데이터를 생성
            val medicineRequest = MedicineRequest(
                memberID = memberID,
                name = (medicineSaveName ?: "Unknown Medicine").toString(),
                meal = mealTime ?: "",
                time = selectedTime ?: "",
                dosage = dosageOnetime,
                memberId = 0 // 고정된 멤버 ID 사용
            )

            saveMedicine(
                memberID = memberID,
                name = medicineName ?: medicineSaveName,
                meal = mealTime ?: "MEAL",
                time = selectedTime ?: "00:00",
                dosage = dosageOnetime,
                memberId = memberIdInt
            )

            Log.d("MedicineRequest", "Request Body: $medicineRequest")
        }

        binding.skippingTv.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun saveMedicine(memberID: String, name: String, meal: String, time: String, dosage: String, memberId: Int) {
        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<MedicineResponse>> = apiService.saveMedicine(
                    memberID = memberID,
                    name = name,
                    meal = meal,
                    time = time,
                    dosage = dosage,
                    memberId = memberId
                )
                if (response.isSuccessful) {
                    Log.d("OnboardingDetailActivity", "Medicine saved successfully: ${response.body()}")
                    startActivity(Intent(this@OnboardingDetailActivity, OnboardingMedicineActivity::class.java))
                } else {
                    Log.e("OnboardingDetailActivity", "Failed to save medicine: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("OnboardingDetailActivity", "Error saving medicine", e)
            }
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
