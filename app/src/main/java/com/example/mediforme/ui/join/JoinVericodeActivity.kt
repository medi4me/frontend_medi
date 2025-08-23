package com.example.mediforme.ui.join

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.AppConfig
import com.example.mediforme.R
import com.example.mediforme.remote.api.PhoneVerificationRequest
import com.example.mediforme.remote.api.PhoneVerificationResponse
import com.example.mediforme.remote.api.Register
import com.example.mediforme.remote.api.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer

class JoinVericodeActivity : AppCompatActivity() {

    private var second = 0
    private var minute = 0
    private var timeTick = 10 // 제한시간 5분을 300초로 설정
    private lateinit var register: Register
    private lateinit var phoneNumber: String
    private lateinit var veriBtn: Button
    private lateinit var timerTv: TextView
    private lateinit var doneTv: TextView
    private lateinit var codeEt: EditText
    private lateinit var remainTv: TextView
    private lateinit var reveri_TV: TextView
    private var countdownTimer: java.util.Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_vericode)

        register = getRetrofit().create(Register::class.java)
        phoneNumber = intent.getStringExtra("phoneNumber") ?: ""

        veriBtn = findViewById(R.id.veri_btn)
        timerTv = findViewById(R.id.timer_TV)
        doneTv = findViewById(R.id.done_TV)
        codeEt = findViewById(R.id.veri_code_ET)
        remainTv = findViewById(R.id.remain_TV)
        remainTv = findViewById(R.id.remain_TV)
        reveri_TV = findViewById(R.id.reveri_TV)

        startTimer()

        veriBtn.setOnClickListener {
            val code = codeEt.text.toString().trim()

            // 공통 입력검증: 6자리 숫자
            if (code.length != 6 || !code.all { it.isDigit() }) {
                Toast.makeText(this, "인증 코드는 6자리 숫자여야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (AppConfig.MOCK_MODE) {
                Toast.makeText(this, "인증에 성공했습니다. (MOCK)", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, JoinIdActivity::class.java))
                finish()
            } else {
                verifyPhoneNumber(phoneNumber, code)
            }
        }

        reveri_TV.setOnClickListener {
            // 서버에 인증법호 재발급 요청
            //

            startTimer()
            Toast.makeText(this, "새 인증번호를 발송했습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun startTimer() {
        second = timeTick % 60
        minute = timeTick / 60

        // 시작 상태 남은시간/타이머 보이기, 만료문구 숨기기
        remainTv.visibility = View.VISIBLE
        timerTv.visibility = View.VISIBLE
        doneTv.visibility = View.GONE
        veriBtn.isEnabled = true

        countdownTimer?.cancel()
        countdownTimer = timer(period = 1000, initialDelay = 1000) {
            runOnUiThread {
                timerTv.text = String.format("0%d : %02d", minute, second)

                if (minute == 0 && second == 0) { remainTv.visibility = View.GONE
                    timerTv.visibility = View.GONE
                    doneTv.visibility = View.VISIBLE
                    veriBtn.isEnabled = false
                    countdownTimer?.cancel()
                    return@runOnUiThread
                }

                if (second == 0) {
                    minute--
                    second = 59
                } else {
                    second--
                }
            }
        }
    }


    private fun verifyPhoneNumber(phone: String, verificationCode: String) {
        val request = PhoneVerificationRequest(phone, verificationCode)
        register.verifyPhone(request).enqueue(object : Callback<PhoneVerificationResponse> {
            override fun onResponse(
                call: Call<PhoneVerificationResponse>,
                response: Response<PhoneVerificationResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true && body.code == "COMMON200") {
                        Toast.makeText(this@JoinVericodeActivity, "인증에 성공했습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@JoinVericodeActivity, JoinIdActivity::class.java))
                        finish()
                    } else if (body?.code == "VERIFICATION_FAILED") {
                        Toast.makeText(this@JoinVericodeActivity, "인증 코드가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PhoneVerificationResponse>, t: Throwable) {
                Toast.makeText(this@JoinVericodeActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer?.cancel()
    }
}
