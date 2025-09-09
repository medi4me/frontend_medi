package com.example.mediforme.ui.join

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mediforme.R
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.request.PhoneVerificationRequest
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.PhoneVerificationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.timer

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class JoinVericodeActivity : AppCompatActivity() {

    private var second = 0
    private var minute = 0
    private var timeTick = 300 // 제한시간 5분을 300초로 설정
    private lateinit var phoneNumber: String

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_vericode)

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

        lifecycleScope.launch {
            try {
                val response: retrofit2.Response<ApiResponse<PhoneVerificationResponse>> = apiService.verifyPhone(request)
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    verificationResponse?.let {
                        if (it.isSuccess && it.code == "COMMON200") {
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
            } catch (e: Exception) {
                Log.e("JoinVericodeActivity", "네트워크 오류", e)
                Toast.makeText(this@JoinVericodeActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
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
