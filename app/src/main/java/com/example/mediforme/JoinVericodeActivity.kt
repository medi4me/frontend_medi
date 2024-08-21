//package com.example.mediforme
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat.startActivity
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import kotlin.concurrent.timer
//
//class JoinVericodeActivity : AppCompatActivity() {
//
//    private var second = 0
//    private var minute = 0
//    private var timeTick = 180 // 예를 들어 3분을 180초로 설정
//    private var generatedCode: String? = null
//    private var phoneNumber: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_join_vericode)
//
//
//        val veri_btn: Button = findViewById(R.id.veri_btn)
//        val veri_code_ET: EditText = findViewById(R.id.veri_code_ET)
//        val timer_TV: TextView = findViewById(R.id.timer_TV)
//        val reveri_TV: TextView = findViewById(R.id.reveri_TV)
//        generatedCode = intent.getStringExtra("generatedCode")
//        //이전 화면에서 받아온 인텐트
//        phoneNumber = intent.getStringExtra("user_phoneNumber")
//
//        setTimer(timer_TV)
//
//        veri_btn.setOnClickListener {
//            val userCode = veri_code_ET.text.toString().trim()
////            if (userCode.isNotEmpty() && userCode == generatedCode) {
////                Toast.makeText(this, "인증 성공", Toast.LENGTH_SHORT).show()
//
//                val intent = Intent(this@JoinVericodeActivity, JoinIdActivity::class.java)
//                intent.putExtra("user_phoneNumber",phoneNumber)
//                intent.putExtra("consent","AGREE")
//                startActivity(intent)
////            } else {
////                Toast.makeText(this, "인증 실패", Toast.LENGTH_SHORT).show()
////            }
//        }
//
//        reveri_TV.setOnClickListener {
//            finish()
//        }
//
//
//    }
//
//    private fun setTimer(textView: TextView) {
//        second = timeTick % 60
//        minute = timeTick / 60
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
////
//////0819 백연결 GPT 참고
////private fun verifyPhoneNumber(phone: String, verificationCode: String) {
////    val apiService = RetrofitClient.instance
////    val request = VerifyPhoneRequest(phone, verificationCode)
////
////    apiService.verifyPhone(request).enqueue(object : Callback<VerifyPhoneResponse> {
////        override fun onResponse(call: Call<VerifyPhoneResponse>, response: Response<VerifyPhoneResponse>) {
////            if (response.isSuccessful) {
////                val body = response.body()
////                if (body != null && body.isSuccess) {
////                    Toast.makeText(this, "인증 성공", Toast.LENGTH_SHORT).show()
////                    val intent = Intent(this, JoinIdActivity::class.java)
////                    intent.putExtra("user_phoneNumber", phone)
////                    intent.putExtra("consent", "AGREE")
////                    startActivity(intent)
////                } else {
////                    Toast.makeText(this, body?.message ?: "인증 실패", Toast.LENGTH_SHORT).show()
////                }
////            } else {
////                Toast.makeText(this, "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
////            }
////        }
////
////        override fun onFailure(call: Call<VerifyPhoneResponse>, t: Throwable) {
////            Toast.makeText(this, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
////        }
////    })
////}


package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.timer

class JoinVericodeActivity : AppCompatActivity() {

    private var second = 0
    private var minute = 0
    private var timeTick = 180 // 예를 들어 5분을 300초로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_vericode)

        setTimer()

        val veri_btn: Button = findViewById(R.id.veri_btn)

        veri_btn.setOnClickListener {
            val intent = Intent(this@JoinVericodeActivity, JoinIdActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setTimer() {
        second = timeTick % 60
        minute = timeTick / 60
        val textView: TextView = findViewById(R.id.timer_TV)

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
