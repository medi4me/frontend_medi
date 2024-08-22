package com.example.mediforme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.Data.PhoneNumberResponse
import com.example.mediforme.Data.Register
import com.example.mediforme.Data.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinPhoneActivity : AppCompatActivity() {

    private lateinit var phone_num_ET: EditText
    private lateinit var veri_btn: Button
    private lateinit var isAvailableTV: TextView
    private lateinit var register: Register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_phone)

        phone_num_ET = findViewById(R.id.phone_num_ET)
        veri_btn = findViewById(R.id.veri_btn)
        isAvailableTV = findViewById(R.id.isAvailable)
        register = getRetrofit().create(Register::class.java)

        veri_btn.isEnabled = false


        phone_num_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phoneNumber = s.toString()
                veri_btn.isEnabled = isPhoneNumberValid(phoneNumber)
                isAvailableTV.visibility = TextView.GONE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        veri_btn.setOnClickListener {
            val phoneNumber = phone_num_ET.text.toString().trim()

            checkPhoneNumber(phoneNumber)
        }
    }

    // 전화번호 유효성을 확인하는 함수
    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.length == 11 && phoneNumber.all { it.isDigit() }
    }

    // 전화번호를 서버에 제출하여 중복 확인
    private fun checkPhoneNumber(phoneNumber: String) {
        register.checkPhoneNumber(phoneNumber).enqueue(object : Callback<PhoneNumberResponse> {
            override fun onResponse(call: Call<PhoneNumberResponse>, response: Response<PhoneNumberResponse>) {
                if (response.isSuccessful) {
                    val phoneResponse = response.body()
                    phoneResponse?.let {
                        if (it.isSuccess) {
                            // Phone number is valid and available
                            isAvailableTV.visibility = TextView.GONE
                            savePhoneNumber(phoneNumber)
                            val intent = Intent(this@JoinPhoneActivity, JoinVericodeActivity::class.java)
                            startActivity(intent)
                        } else if (it.code == "PHONE_FOUND") {
                            isAvailableTV.visibility = TextView.VISIBLE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PhoneNumberResponse>, t: Throwable) {
              Log.d("JoinPhoneActivity", "네트워크 오류!")
            }
        })
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
