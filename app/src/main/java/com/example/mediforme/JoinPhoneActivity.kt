package com.example.mediforme

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlin.random.Random

class JoinPhoneActivity : AppCompatActivity() {

    private lateinit var phone_num_ET: EditText
    private lateinit var veri_btn: Button
    private var generatedCode: String? = null

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
                val message = phone_num_ET.text.toString()
                veri_btn.isEnabled = message.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        veri_btn.setOnClickListener {
            val phoneNumber = phone_num_ET.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                generatedCode = generateVerificationCode()
                sendSms(phoneNumber, generatedCode!!)
                Toast.makeText(this, "인증번호가 발송되었습니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@JoinPhoneActivity, JoinVericodeActivity::class.java)
                //인텐트에 정보를 담아서 다음 화면(액티비티로 정보 전달)
                intent.putExtra("generatedCode", generatedCode)
                intent.putExtra("user_phoneNumber", phoneNumber)
                intent.putExtra("consent","AGREE")
                startActivity(intent)
            } else {
                Toast.makeText(this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 권한 요청
        requestSmsPermissions()
    }

    private fun generateVerificationCode(): String {
        return Random.nextInt(100000, 999999).toString()
    }

    private fun sendSms(phoneNumber: String, code: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, "인증번호는 $code 입니다.", null, null)
    }

    private fun requestSmsPermissions() {
        val permission = Manifest.permission.SEND_SMS
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
        }
    }
}

//package com.example.mediforme
//
//import android.content.Intent
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.widget.Button
//import android.widget.EditText
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//
//class JoinPhoneActivity : AppCompatActivity() {
//
//    private lateinit var phone_num_ET: EditText
//    private lateinit var veri_btn: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_join_phone)
//
//        phone_num_ET = findViewById(R.id.phone_num_ET)
//        veri_btn = findViewById(R.id.veri_btn)
//
//        veri_btn.isEnabled = false
//
//
//        phone_num_ET.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val message = phone_num_ET.text.toString()
//                veri_btn.isEnabled = message.isNotEmpty()
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//
//        veri_btn.setOnClickListener {
//            val intent = Intent(this@JoinPhoneActivity, JoinVericodeActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//}
