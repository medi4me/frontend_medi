package com.example.mediforme.ui.search

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
import com.example.mediforme.databinding.FragmentAddMedicineBinding
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.MedicineResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class AddMedicineResultActivity : AppCompatActivity() {
    private lateinit var binding: FragmentAddMedicineBinding
    private var selectedTime: String? = null // 선택된 시간 저장 변수
    private var selectedMealTime: String? = null // 선택된 식사 시간 저장 변수
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var memberID: String

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // SharedPreferences에서 memberID 가져오기
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        memberID = sharedPreferences.getString("memberID", null) ?: ""
        val memberIdInt = sharedPreferences.getString("memberId", "0")?.toIntOrNull() ?: 0

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

            // selectedMealTime 값을 변환
            val mealTime = when (selectedMealTime) {
                "식전" -> "NOMEAL"
                "식후" -> "MEAL"
                else -> selectedMealTime ?: "MEAL"
            }

            // MedicineRequest에서 필요한 데이터를 개별적으로 추출
            val medicineSaveName = binding.medicineName.text.toString()
            val selectedTime = selectedTime ?: "00:00"

            saveMedicine(
                memberID = memberID,
                name = medicineSaveName,
                meal = mealTime,
                time = selectedTime,
                dosage = dosageOnetime,
                memberId = memberIdInt
            )
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
                    // 서버로부터 성공적으로 응답을 받았을 때 처리
                    Log.d("AddMedicineResultActivity", "Medicine saved successfully: ${response.body()}")
                    startActivity(Intent(this@AddMedicineResultActivity, MainActivity::class.java))
                } else {
                    // 서버로부터 응답이 왔지만 성공하지 않은 경우 처리
                    Log.e("AddMedicineResultActivity", "Failed to save medicine: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // 요청이 실패했을 때 처리
                Log.e("AddMedicineResultActivity", "Error saving medicine", e)
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
