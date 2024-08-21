package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class JoinIdActivity : AppCompatActivity() {
    private lateinit var user_id_ET: EditText
    private lateinit var nextBtn: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_id)

        nextBtn = findViewById(R.id.next_btn)
        user_id_ET = findViewById(R.id.user_id_ET)

        nextBtn.isEnabled = false  // 초기 상태는 비활성화

        user_id_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userId = s.toString()
                nextBtn.isEnabled = isUserIdValid(userId)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        nextBtn.setOnClickListener {
            val user_id = user_id_ET.text.toString().trim()

            val intent = Intent(this@JoinIdActivity, JoinPasswordActivity::class.java).apply {
                putExtra("user_id",user_id)
                putExtra("consent","AGREE")
            }
            startActivity(intent)
        }
    }
    // 아이디 유효성을 확인하는 함수
    private fun isUserIdValid(userId: String): Boolean {
        // 영문자와 숫자가 모두 포함되어 있고, 길이가 5~30자인지 확인하는 정규식
        val regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,30}$".toRegex()
        return regex.matches(userId)
    }
}

