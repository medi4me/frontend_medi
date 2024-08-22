package com.example.mediforme.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.Data.AuthService
import com.example.mediforme.Data.LoginRequest
import com.example.mediforme.Data.LoginResponse
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.JoinServiceActivity
import com.example.mediforme.MainActivity
import com.example.mediforme.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        // View Binding 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // authService 초기화
        authService = getRetrofit().create(AuthService::class.java)

        // 아이디 찾기 텍스트뷰 클릭 리스너
        binding.searchIdTV.setOnClickListener {
            startActivity(Intent(this, SearchmainActivity::class.java))
        }

        // 비밀번호 찾기 텍스트뷰 클릭 리스너
        binding.searchPasswordTV.setOnClickListener {
            startActivity(Intent(this, SearchmainActivity::class.java))
        }

        binding.joinTV.setOnClickListener {
            startActivity(Intent(this, JoinServiceActivity::class.java))
        }

        // 로그인 버튼 클릭 리스너
        binding.veriBtn.setOnClickListener {
            val id = binding.idET.text.toString()
            val password = binding.passwordET.text.toString()


            // Retrofit을 사용하여 로그인 API 호출
            val loginRequest = LoginRequest(
                memberID = id,
                password = password
            )

            authService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        loginResponse?.let {
                            if (it.isSuccess) {
                                Log.d("LoginActivity", "Login Successful: ${it.result.accessToken}")
                                // Access token과 같은 데이터를 저장하거나 다음 화면으로 이동하는 로직 추가
                                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                                // 로그인 성공 시 MainActivity로 이동
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.e("LoginActivity", "Login Failed: ${it.message}")
                                Toast.makeText(this@LoginActivity, "로그인 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.e("LoginActivity", "Response Error: ${response.code()}")
                        Toast.makeText(this@LoginActivity, "로그인 실패: 서버 오류", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginActivity", "Network Error: ${t.message}")
                    Toast.makeText(this@LoginActivity, "로그인 실패: 네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
}
