package com.example.mediforme.ui.join

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.AppConfig
import com.example.mediforme.remote.api.Register
import com.example.mediforme.remote.api.RegisterResponse
import com.example.mediforme.remote.api.RegisterUserData
import com.example.mediforme.remote.api.getRetrofit
import com.example.mediforme.R
import com.example.mediforme.ui.login.LoginActivity
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

            // 이름 유효성 검사
            if (!isValidName(user_name)) {
                Toast.makeText(this, "이름은 2~10자의 한글, 영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (AppConfig.MOCK_MODE) {
                // MOCK 모드
                Toast.makeText(this, "회원가입 성공 (MOCK)", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // 회원가입 처리함수 호출, (API 연결 함수)!!
                registerUser(user_name, user_password, phoneNumber, user_id, consent)
            }
        }
    }

    // 이름 유효성 함수
    private fun isValidName(name: String): Boolean {
        // 2~10자, 한글/영문/숫자만 허용
        val regex = "^[가-힣a-zA-Z0-9]{2,10}$".toRegex()
        return regex.matches(name)
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
                            // 회원가입 성공 시
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