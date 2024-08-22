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
import com.example.mediforme.Data.MedicineSaveService
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
    private val memberId: Int = 1 // 고정된 멤버 ID

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

            // selectedMealTime 값을 변환
            val mealTime = when (selectedMealTime) {
                "식전" -> "NOMEAL"
                "식후" -> "MEAL"
                else -> selectedMealTime // 만약 다른 값이 있으면 원래 값을 그대로 사용
            }

            val medicineSaveName = binding.medicineNameTv.text

            // 선택된 시간, 식사 시간, 복용량을 로그로 출력
            Log.d("OnboardingDetailActivity", "Medicine Name: $medicineSaveName")
            Log.d("OnboardingDetailActivity", "Selected Time: $selectedTime")
            Log.d("OnboardingDetailActivity", "Selected Meal Time: $mealTime")
            Log.d("OnboardingDetailActivity", "Dosage One-time: $dosageOnetime")


            //startActivity(Intent(this, OnboardingMedicineActivity::class.java))
            // MedicineRequest 데이터 클래스에 필요한 데이터를 생성
            val medicineRequest = MedicineRequest(
                name = (medicineSaveName ?: "Unknown Medicine").toString(),
                meal = mealTime ?: "",
                time = selectedTime ?: "",
                dosage = dosageOnetime,
                memberId = memberId // 고정된 멤버 ID 사용
            )

            // Retrofit 인스턴스 생성 및 서비스 인터페이스 초기화
            val retrofit = getRetrofit()
            val service = retrofit.create(MedicineSaveService::class.java)

            // POST 요청을 서버로 보내기
            val call = service.saveMedicine(
                name = medicineName ?: "Unknown Medicine",
                meal = mealTime ?: "MEAL",
                time = selectedTime ?: "00:00",
                dosage = dosageOnetime,
                memberId = memberId
            )
            Log.d("MedicineRequest", "Request Body: $medicineRequest")
            call.enqueue(object : Callback<MedicineResponse> {
                override fun onResponse(call: Call<MedicineResponse>, response: Response<MedicineResponse>) {
                    if (response.isSuccessful) {
                        // 서버로부터 성공적으로 응답을 받았을 때 처리
                        Log.d("OnboardingDetailActivity", "Medicine saved successfully: ${response.body()}")
                        startActivity(Intent(this@OnboardingDetailActivity, OnboardingMedicineActivity::class.java))
                    } else {
                        // 서버로부터 응답이 왔지만 성공하지 않은 경우 처리
                        Log.e("OnboardingDetailActivity", "Failed to save medicine: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<MedicineResponse>, t: Throwable) {
                    // 요청이 실패했을 때 처리
                    Log.e("OnboardingDetailActivity", "Error saving medicine", t)
                }
            })
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
            selectedTime = String.format("%02d:%02d", hour, minute) // 선택된 시간을 변수에 저장
            binding.doseTimeSpinner.text = selectedTime
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
