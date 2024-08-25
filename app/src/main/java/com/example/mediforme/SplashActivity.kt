package com.example.mediforme

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        enableEdgeToEdge()

        // 동일한 SharedPreferences 파일 이름 사용
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        // 3초 동안 대기한 후 다음 액티비티로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            // 로그인 상태 확인
            val memberID = sharedPreferences.getString("memberID", null)
            Log.d("SplashActivity", "읽은 memberID: $memberID")

            val intent = if (memberID != null) {
                // 로그인 상태라면 MainActivity로 이동
                Intent(this@SplashActivity, MainActivity::class.java)
            } else {
                // 로그인 상태가 아니라면 SplashChoiceActivity로 이동
                Intent(this@SplashActivity, SplashChoiceActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, 3000) // 3000ms = 3초
    }
}
