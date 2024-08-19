package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediforme.onboarding.OnboardingAgeActivity
import com.example.mediforme.onboarding.OnboardingMedicineActivity

class JoinNameActivity : AppCompatActivity() {
    private lateinit var user_name_ET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_name)

        //이전 화면에서 받아온 인텐트
        val phoneNumber = intent.getStringExtra("user_phoneNumber").toString()
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

            val intent = Intent(this, OnboardingMedicineActivity::class.java)
            startActivity(intent)
        }
    }
    //회원가입 처리함수
    private fun registerUser(name: String?, password: String?, phone: String?, memberID: String, consent: String) {
        Log.d("Register", "이름: $name\n비번: $password\n전번: $phone,\n아이디: $memberID,\n동의 여부: $consent  ")

        // 여기서 서버로 회원가입 요청을 보낼 수 있습니다.
        // 예: Retrofit을 사용하여 API 호출 등

    }

}