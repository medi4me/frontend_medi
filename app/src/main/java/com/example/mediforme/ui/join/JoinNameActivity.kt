package com.example.mediforme.ui.join

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mediforme.R
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.request.RegisterUserData
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.RegisterResponse
import com.example.mediforme.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class JoinNameActivity : AppCompatActivity() {
    private lateinit var user_name_ET: EditText

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_name)

        //이전 화면에서 받아온 인텐트
        val phoneNumber = getPhoneNumber()!!
        val user_id = intent.getStringExtra("user_id").toString()
        val user_password = intent.getStringExtra("user_password").toString()
        val consent = intent.getStringExtra("consent").toString()

////        //인텐트 값 넘어왔는지 확인
//       val confirm = findViewById<TextView>(R.id.confirm)
//       confirm.text = "폰 넘버: $phoneNumber\n유저 아이디: $user_id\n유저 비밀번호: $user_password\n필수 동의여부: $consent"

        val nextBtn: Button = findViewById(R.id.next_btn)
        user_name_ET = findViewById(R.id.user_name_ET)

        // 회원가입 버튼
        nextBtn.setOnClickListener {
            val user_name = user_name_ET.text.toString()
            //회원가입 처리함수 호출, (API 연결 함수)!!
            registerUser(user_name,user_password,phoneNumber,user_id,consent)
//
//            val intent = Intent(this, OnboardingMedicineActivity::class.java)
//            startActivity(intent)
        }
    }

    //회원가입 처리함수
    private fun registerUser(name: String, password: String, phone: String, memberID: String, consent: String) {
        Log.d("Register", "이름: $name\n비번: $password\n전번: $phone,\n아이디: $memberID,\n동의 여부: $consent")

        val userData = RegisterUserData(
            name = name,
            password = password,
            phone = phone,
            memberID = memberID,
            consent = consent
        )

        lifecycleScope.launch {
            try {
                val response: retrofit2.Response<ApiResponse<RegisterResponse>> = apiService.registerUser(userData)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isSuccess) {
                            Log.d("Register", "회원가입 성공: ${it.message}")
                            val intent = Intent(this@JoinNameActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.d("Register", "회원가입 실패: ${it.message}")
                        }
                    }
                } else {
                    Log.d("Register", "서버 응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Register", "회원가입 요청 실패", e)
            }
        }
    }
    // SharedPreferences에서 전화번호를 가져오는 함수
    private fun getPhoneNumber(): String? {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("user_phoneNumber", null)
    }
}