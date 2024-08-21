package com.example.mediforme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediforme.Data.Register
import com.example.mediforme.Data.RegisterResponse
import com.example.mediforme.Data.RegisterUserData
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.onboarding.OnboardingAgeActivity
import com.example.mediforme.onboarding.OnboardingMedicineActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinNameActivity : AppCompatActivity() {
    private lateinit var user_name_ET: EditText

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

        val retrofit = getRetrofit()
        val apiService = retrofit.create(Register::class.java)

        val userData = RegisterUserData(
            name = name,
            password = password,
            phone = phone,
            memberID = memberID,
            consent = consent
        )

        apiService.registerUser(userData).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isSuccess) {
                            Log.d("Register", "회원가입 성공: ${it.message}")
                            val intent = Intent(this@JoinNameActivity, OnboardingMedicineActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.d("Register", "회원가입 실패: ${it.message}")
                        }
                    }
                } else {
                    Log.d("Register", "서버 응답 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("Register", "회원가입 요청 실패", t)
            }
        })
    }
    // SharedPreferences에서 전화번호를 가져오는 함수
    private fun getPhoneNumber(): String? {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("user_phoneNumber", null)
    }
}