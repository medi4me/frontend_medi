package com.example.mediforme.ui.join

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.AppConfig
import com.example.mediforme.remote.api.MemberIDRequest
import com.example.mediforme.remote.api.MemberIDResponse
import com.example.mediforme.remote.api.Register
import com.example.mediforme.remote.api.getRetrofit
import com.example.mediforme.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinIdActivity : AppCompatActivity() {
    private lateinit var user_id_ET: EditText
    private lateinit var nextBtn: Button
    private lateinit var resisterService: Register
    private lateinit var idCheckImpossibleTV: TextView
    private lateinit var idCheckPossibleTV: TextView
    private lateinit var alertIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_id)

        resisterService = getRetrofit().create(Register::class.java)
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
                // 입력 변경 시 안내문구/아이콘 초기화
                idCheckImpossibleTV.visibility = View.GONE
                idCheckPossibleTV.visibility = View.GONE
                alertIcon.visibility = View.GONE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        nextBtn.setOnClickListener {
            val userId = user_id_ET.text.toString().trim()

            if (!isUserIdValid(userId)) {
                idCheckImpossibleTV.text = "아이디 형식이 올바르지 않습니다."
                idCheckImpossibleTV.visibility = View.VISIBLE
                idCheckPossibleTV.visibility = View.GONE
                alertIcon.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Mock모드
            if (AppConfig.MOCK_MODE) {
                // mock모드 존재 아이디-> admin/test/user로 시작하면 중복 처리
                val isDuplicateMock =
                    userId.startsWith("admin", ignoreCase = true) ||
                            userId.startsWith("test", ignoreCase = true) ||
                            userId.startsWith("user", ignoreCase = true)

                if (isDuplicateMock) {
                    idCheckImpossibleTV.text = "이미 존재하는 아이디 입니다.(MOCK)"
                    idCheckImpossibleTV.visibility = View.VISIBLE
                    idCheckPossibleTV.visibility = View.GONE
                    alertIcon.visibility = View.VISIBLE
                } else {
                    idCheckPossibleTV.text = "사용가능한 아이디입니다.(MOCK)"
                    idCheckPossibleTV.visibility = View.VISIBLE
                    idCheckImpossibleTV.visibility = View.GONE
                    alertIcon.visibility = View.GONE

                    val intent = Intent(this@JoinIdActivity, JoinPasswordActivity::class.java).apply {
                        putExtra("user_id", userId)
                        putExtra("consent", "AGREE")
                    }
                    startActivity(intent)
                }
            } else {
                // 실서버 모드
                checkMemberID(userId)
            }
        }
    }

    // 아이디 유효성 (영문+숫자 포함, 5~30자)
    private fun isUserIdValid(userId: String): Boolean {
        val regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,30}$".toRegex()
        return regex.matches(userId)
    }

    // 서버 중복확인
    private fun checkMemberID(userId: String) {
        // 사용자가 입력한 memberID를 포함한 요청 생성
        val request = MemberIDRequest(
            memberID = userId
            // 다른 필드는 기본값으로 설정됨
        )

        resisterService.checkMemberID(request).enqueue(object : Callback<MemberIDResponse> {
            override fun onResponse(call: Call<MemberIDResponse>, response: Response<MemberIDResponse>) {
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
            }

            override fun onFailure(call: Call<MemberIDResponse>, t: Throwable) {
                idCheckImpossibleTV.text = "네트워크 오류가 발생했습니다."
                idCheckImpossibleTV.visibility = View.VISIBLE
                idCheckPossibleTV.visibility = View.GONE
                alertIcon.visibility = View.VISIBLE
            }
        })
    }
}
