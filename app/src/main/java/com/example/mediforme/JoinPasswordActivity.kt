package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class JoinPasswordActivity : AppCompatActivity() {

    private lateinit var passwordET: EditText
    private lateinit var criteriaLength: TextView
    private lateinit var criteriaNumber: TextView
    private lateinit var criteriaLetter: TextView
    private lateinit var criteriaSpecial: TextView
    private lateinit var nextBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_password)

        //이전 화면에서 받아온 인텐트
        val phoneNumber = intent.getStringExtra("user_phoneNumber")
        val user_id = intent.getStringExtra("user_id")

        passwordET = findViewById(R.id.password_ET)
        criteriaLength = findViewById(R.id.criteria_length)
        criteriaNumber = findViewById(R.id.criteria_number)
        criteriaLetter = findViewById(R.id.criteria_letter)
        criteriaSpecial = findViewById(R.id.criteria_special)
        nextBtn = findViewById(R.id.next_btn)

        nextBtn.isEnabled = false // 초기 상태에서 버튼 비활성화

        passwordET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        nextBtn.setOnClickListener {
            val user_password = passwordET.text.toString().trim()

            val intent = Intent(this@JoinPasswordActivity, JoinNameActivity::class.java).apply {
                putExtra("user_id",user_id)
                putExtra("user_phoneNumber",phoneNumber)
                putExtra("user_password",user_password)
            }
            startActivity(intent)
        }
    }

    private fun validatePassword(password: String) {
        var isValid = true

        // Check length
        if (password.length >= 8) {
            criteriaLength.background = ContextCompat.getDrawable(this, R.drawable.background_pwcheck)
            criteriaLength.setTextColor(resources.getColor(R.color.active_btn))
        } else {
            criteriaLength.background = ContextCompat.getDrawable(this, R.drawable.background_pwwrong)
            criteriaLength.setTextColor(resources.getColor(R.color.light_gray))
            isValid = false
        }

        // Check for number
        if (password.any { it.isDigit() }) {
            criteriaNumber.background = ContextCompat.getDrawable(this, R.drawable.background_pwcheck)
            criteriaNumber.setTextColor(resources.getColor(R.color.active_btn))
        } else {
            criteriaNumber.background = ContextCompat.getDrawable(this, R.drawable.background_pwwrong)
            criteriaNumber.setTextColor(resources.getColor(R.color.light_gray))
            isValid = false
        }

        // Check for letter
        if (password.any { it.isLetter() }) {
            criteriaLetter.background = ContextCompat.getDrawable(this, R.drawable.background_pwcheck)
            criteriaLetter.setTextColor(resources.getColor(R.color.active_btn))
        } else {
            criteriaLetter.background = ContextCompat.getDrawable(this, R.drawable.background_pwwrong)
            criteriaLetter.setTextColor(resources.getColor(R.color.light_gray))
            isValid = false
        }

        // Check for special character
        if (password.any { !it.isLetterOrDigit() }) {
            criteriaSpecial.background = ContextCompat.getDrawable(this, R.drawable.background_pwcheck)
            criteriaSpecial.setTextColor(resources.getColor(R.color.active_btn))
        } else {
            criteriaSpecial.background = ContextCompat.getDrawable(this, R.drawable.background_pwwrong)
            criteriaSpecial.setTextColor(resources.getColor(R.color.light_gray))
            isValid = false
        }

        // Check if the password is empty
        if (password.isEmpty()) {
            isValid = false
        }

        nextBtn.isEnabled = isValid
    }
}
