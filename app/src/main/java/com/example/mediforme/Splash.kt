package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 3초 동안 대기한 후 join_phone 액티비티로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@Splash, Join_phone::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000ms = 3초
    }
}
