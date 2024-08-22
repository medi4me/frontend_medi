package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.Data.PhoneVerificationRequest
import com.example.mediforme.Data.PhoneVerificationResponse
import com.example.mediforme.Data.Register
import com.example.mediforme.Data.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer

class JoinVericodeActivity : AppCompatActivity() {

    private var second = 0
    private var minute = 0
    private var timeTick = 300 // 제한시간 5분을 300초로 설정
    private lateinit var register: Register
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_vericode)

        register = getRetrofit().create(Register::class.java)
        phoneNumber = intent.getStringExtra("user_phoneNumber") ?: ""

        setTimer()

        val veri_btn: Button = findViewById(R.id.veri_btn)
        val reveri_TV: TextView = findViewById(R.id.reveri_TV)
        val veri_code_ET: EditText = findViewById(R.id.veri_code_ET)

        reveri_TV.setOnClickListener {
            finish()
        }

        veri_btn.setOnClickListener {
            val verificationCode = veri_code_ET.text.toString().trim()
            verifyPhoneNumber(phoneNumber, verificationCode)
        }
    }

    private fun verifyPhoneNumber(phone: String, verificationCode: String) {
        val request = PhoneVerificationRequest(phone, verificationCode)

        register.verifyPhone(request).enqueue(object : Callback<PhoneVerificationResponse> {
            override fun onResponse(call: Call<PhoneVerificationResponse>, response: Response<PhoneVerificationResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    verificationResponse?.let {
                        if (it.isSuccess) {
                            // 인증 성공 시 다음 화면으로 이동
                            Toast.makeText(this@JoinVericodeActivity, "인증에 성공했습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@JoinVericodeActivity, JoinIdActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else if (it.code == "VERIFICATION_FAILED") {
                            // 인증 실패 메시지 표시
                            Toast.makeText(this@JoinVericodeActivity, "인증 코드가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PhoneVerificationResponse>, t: Throwable) {
                Toast.makeText(this@JoinVericodeActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
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
