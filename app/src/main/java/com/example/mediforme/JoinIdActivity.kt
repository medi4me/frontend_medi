package com.example.mediforme

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
import com.example.mediforme.Data.AuthService
import com.example.mediforme.Data.MemberIDRequest
import com.example.mediforme.Data.MemberIDResponse
import com.example.mediforme.Data.Register
import com.example.mediforme.Data.getRetrofit
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
                // 에러 처리, 예를 들어 토스트 메시지 표시
            }
        })
    }
}