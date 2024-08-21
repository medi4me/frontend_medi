package com.example.mediforme.login

import JoinServiceActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        // View Binding 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 아이디 찾기 텍스트뷰 클릭 리스너
        binding.searchIdTV.setOnClickListener {
            startActivity(Intent(this, SearchmainActivity::class.java))
        }

        // 비밀번호 찾기 텍스트뷰 클릭 리스너
        binding.searchPasswordTV.setOnClickListener {
            startActivity(Intent(this, SearchmainActivity::class.java))
        }

        binding.joinTV.setOnClickListener {
            startActivity(Intent(this, JoinServiceActivity::class.java))
        }
    }
}
