package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.timer

class JoinVericodeActivity : AppCompatActivity() {

    private var second = 0
    private var minute = 0
    private var timeTick = 180 // 예를 들어 3분을 180초로 설정
    private var generatedCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_vericode)

        //이전 화면에서 받아온 인텐트
        val phoneNumber = intent.getStringExtra("user_phoneNumber")

        val veri_btn: Button = findViewById(R.id.veri_btn)
        val veri_code_ET: EditText = findViewById(R.id.veri_code_ET)
        val timer_TV: TextView = findViewById(R.id.timer_TV)
        generatedCode = intent.getStringExtra("generatedCode")

        setTimer(timer_TV)

        veri_btn.setOnClickListener {
            val userCode = veri_code_ET.text.toString().trim()
            if (userCode.isNotEmpty() && userCode == generatedCode) {
                Toast.makeText(this, "인증 성공", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@JoinVericodeActivity, JoinIdActivity::class.java)
                intent.putExtra("user_phoneNumber",phoneNumber)
                startActivity(intent)
            } else {
                Toast.makeText(this, "인증 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setTimer(textView: TextView) {
        second = timeTick % 60
        minute = timeTick / 60

        timer(period = 1000, initialDelay = 1000) {
            runOnUiThread {
                textView.text = String.format("0%d : %02d", minute, second)
                if (second == 0) {
                    if (minute == 0) {
                        cancel() // 타이머 종료
                    } else {
                        minute--
                        second = 60
                    }
                }
                second--
            }
        }
    }
}

//package com.example.mediforme
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import kotlin.concurrent.timer
//
//class JoinVericodeActivity : AppCompatActivity() {
//
//    private var second = 0
//    private var minute = 0
//    private var timeTick = 180 // 예를 들어 5분을 300초로 설정
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_join_vericode)
//
//        setTimer()
//
//        val veri_btn: Button = findViewById(R.id.veri_btn)
//
//        veri_btn.setOnClickListener {
//            val intent = Intent(this@JoinVericodeActivity, JoinIdActivity::class.java)
//            startActivity(intent)
//        }
//    }
//
//    private fun setTimer() {
//        second = timeTick % 60
//        minute = timeTick / 60
//        val textView: TextView = findViewById(R.id.timer_TV)
//
//        timer(period = 1000, initialDelay = 1000) {
//            runOnUiThread {
//                textView.text = String.format("0%d : %02d", minute, second)
//                if (second == 0) {
//                    if (minute == 0) {
//                        cancel() // 타이머 종료
//                    } else {
//                        minute--
//                        second = 60
//                    }
//                }
//                second--
//            }
//        }
//    }
//}
