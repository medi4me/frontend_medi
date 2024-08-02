package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class JoinServiceActivity : AppCompatActivity() {

    private lateinit var checkBox: CheckBox
    private lateinit var smallCheck1: CheckBox
    private lateinit var smallCheck2: CheckBox
    private lateinit var smallCheck3: CheckBox
    private lateinit var smallCheck4: CheckBox
    private lateinit var veriBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_service)

        veriBtn = findViewById(R.id.veri_btn)

        veriBtn.setOnClickListener {
            if (smallCheck1.isChecked && smallCheck2.isChecked && smallCheck3.isChecked) {
                val intent = Intent(this@JoinServiceActivity, JoinPhoneActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "필수 약관에 모두 동의해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        checkBox = findViewById(R.id.check_box)
        smallCheck1 = findViewById(R.id.small_check1)
        smallCheck2 = findViewById(R.id.small_check2)
        smallCheck3 = findViewById(R.id.small_check3)
        smallCheck4 = findViewById(R.id.small_check4)

        setupCheckBoxListeners()
        updateButtonState()  // 초기 상태 설정

    }

    private fun setupCheckBoxListeners() {
        // 전체 동의 체크박스 클릭 리스너
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            smallCheck1.isChecked = isChecked
            smallCheck2.isChecked = isChecked
            smallCheck3.isChecked = isChecked
            smallCheck4.isChecked = isChecked
            updateButtonState()
        }

        val smallCheckBoxes = listOf(smallCheck1, smallCheck2, smallCheck3, smallCheck4)
        smallCheckBoxes.forEach { smallCheckBox ->
            smallCheckBox.setOnCheckedChangeListener { _, _ ->
                updateMainCheckBox()
                updateButtonState()
            }
        }
    }

    private fun updateMainCheckBox() {
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = smallCheck1.isChecked && smallCheck2.isChecked && smallCheck3.isChecked && smallCheck4.isChecked
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            smallCheck1.isChecked = isChecked
            smallCheck2.isChecked = isChecked
            smallCheck3.isChecked = isChecked
            smallCheck4.isChecked = isChecked
            updateButtonState()
        }
    }

    private fun updateButtonState() {
        veriBtn.isEnabled = smallCheck1.isChecked && smallCheck2.isChecked && smallCheck3.isChecked
    }
}