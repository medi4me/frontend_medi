package com.example.mediforme.ui.join

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mediforme.R
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.request.MemberIDRequest
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.MemberIDResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class JoinIdActivity : AppCompatActivity() {
    private lateinit var user_id_ET: EditText
    private lateinit var nextBtn: Button
    private lateinit var idCheckImpossibleTV: TextView
    private lateinit var idCheckPossibleTV: TextView
    private lateinit var alertIcon: ImageView

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_id)

        nextBtn = findViewById(R.id.next_btn)
        user_id_ET = findViewById(R.id.user_id_ET)
        idCheckImpossibleTV = findViewById(R.id.id_check_impossible_TV)
        idCheckPossibleTV = findViewById(R.id.id_check_poissible_TV)
        alertIcon = findViewById(R.id.alertIcon)

        nextBtn.isEnabled = false  // 초기 상태는 비활성화

        user_id_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userId = s.toString()
                nextBtn.isEnabled = isUserIdValid(userId)
                idCheckImpossibleTV.visibility = View.GONE
                idCheckPossibleTV.visibility = View.GONE
                alertIcon.visibility = View.GONE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        nextBtn.setOnClickListener {
            val userId = user_id_ET.text.toString().trim()
            checkMemberID(userId)
        }
    }

    // 아이디 유효성을 확인하는 함수
    private fun isUserIdValid(userId: String): Boolean {
        // 영문자와 숫자가 모두 포함되어 있고, 길이가 5~30자인지 확인하는 정규식
        val regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,30}$".toRegex()
        return regex.matches(userId)
    }
    private fun checkMemberID(userId: String) {
        // 사용자가 입력한 memberID를 포함한 요청 생성
        val request = MemberIDRequest(
            memberID = userId
            // 다른 필드는 기본값으로 설정됨
        )

        lifecycleScope.launch {
            try {
                val response: retrofit2.Response<ApiResponse<MemberIDResponse>> = apiService.checkMemberID(request)
                if (response.isSuccessful) {
                    val memberIDResponse = response.body()
                    memberIDResponse?.let {
                        if (it.isSuccess) {
                            // ID 사용 가능
                            idCheckPossibleTV.visibility = View.VISIBLE
                            idCheckImpossibleTV.visibility = View.GONE
                            alertIcon.visibility = View.GONE

                            val intent = Intent(this@JoinIdActivity, JoinPasswordActivity::class.java).apply {
                                putExtra("user_id", userId)
                                putExtra("consent", "AGREE")
                            }
                            startActivity(intent)
                        } else if (it.code == "DUPLICATE_MEMBER_ID") {
                            // ID 중복
                            idCheckImpossibleTV.visibility = View.VISIBLE
                            idCheckPossibleTV.visibility = View.GONE
                            alertIcon.visibility = View.VISIBLE
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("JoinIdActivity", "네트워크 오류", e)
                // 오류 메시지 표시
            }
        }
    }
}