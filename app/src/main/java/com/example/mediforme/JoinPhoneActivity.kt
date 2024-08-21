package com.example.mediforme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class JoinPhoneActivity : AppCompatActivity() {

    private lateinit var phone_num_ET: EditText
    private lateinit var veri_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_phone)

        phone_num_ET = findViewById(R.id.phone_num_ET)
        veri_btn = findViewById(R.id.veri_btn)

        veri_btn.isEnabled = false


        phone_num_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phoneNumber = s.toString()
                veri_btn.isEnabled = isPhoneNumberValid(phoneNumber)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        veri_btn.setOnClickListener {
            val phoneNumber = phone_num_ET.text.toString().trim()

            // SharedPreferences에 전화번호 저장
            savePhoneNumber(phoneNumber)

            val intent = Intent(this@JoinPhoneActivity, JoinVericodeActivity::class.java)
            startActivity(intent)
        }
    }

    // 전화번호 유효성을 확인하는 함수
    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 11 && phoneNumber.all { it.isDigit() }
    }

    // 전화번호를 SharedPreferences에 저장하는 함수
    private fun savePhoneNumber(phoneNumber: String) {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_phoneNumber", phoneNumber)
            apply()  // 비동기 저장
        }
    }
}
