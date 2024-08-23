package com.example.mediforme.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.Data.AuthService
import com.example.mediforme.Data.LoginRequest
import com.example.mediforme.Data.LoginResponse
import com.example.mediforme.Data.NameResponse
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.JoinServiceActivity
import com.example.mediforme.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class LoginActivity : AppCompatActivity() {

    private lateinit var passwordET: EditText
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authService: AuthService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewPasswordIv: ImageView
    private lateinit var hidePasswordIv: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // View Binding 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // authService 초기화
        authService = getRetrofit().create(AuthService::class.java)
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        passwordET = findViewById(R.id.password_ET)
        viewPasswordIv = findViewById(R.id.sign_up_view_password_iv)
        hidePasswordIv = findViewById(R.id.sign_up_hide_password_iv)


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
                                // 로그인 성공 시, 토큰을 저장하고 다음 작업 진행
                                saveTokensAndProceed(id, it.result.accessToken, it.result.refreshToken)
                            } else {
                                Log.e("LoginActivity", "Login Failed: ${it.message}")
                                loginFailDialog()
                            }
                        }
                    } else {
                        Log.e("LoginActivity", "Response Error: ${response.code()}")
                        loginFailDialog()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginActivity", "Network Error: ${t.message}")
                    Toast.makeText(this@LoginActivity, "로그인 실패: 네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun saveTokensAndProceed(memberID: String, accessToken: String, refreshToken: String) {
        // SharedPreferences에 저장
        val editor = sharedPreferences.edit()
        editor.putString("memberID", memberID)
        editor.putString("accessToken", accessToken)
        editor.putString("refreshToken", refreshToken)
        editor.apply()

        // 로그인 성공 후 이름 저장
        fetchUserNameAndSave(memberID)
    }

    // 로그인 실패 시 다이얼로그 표시 메서드
    private fun loginFailDialog() {
        val dialogView1 = LayoutInflater.from(this).inflate(R.layout.dialog_not_collect, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView1)
            .setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val backBtn = dialogView1.findViewById<ImageView>(R.id.dialog_not_collect_xBtn_IV)
        val okBtn = dialogView1.findViewById<Button>(R.id.dialog_not_collect_ok_BTN)

        backBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        okBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun fetchUserNameAndSave(memberID: String) {
        authService.getName(memberID).enqueue(object : Callback<NameResponse> {
            override fun onResponse(call: Call<NameResponse>, response: Response<NameResponse>) {
                if (response.isSuccessful) {
                    val nameResponse = response.body()
                    nameResponse?.let {
                        if (it.isSuccess) {
                            val editor = sharedPreferences.edit()
                            editor.putString("name", it.result)
                            editor.apply()

                            Log.d("LoginActivity", "Name fetched and saved: ${it.result}")
                            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e("LoginActivity", "Failed to fetch name: ${it.message}")
                            Toast.makeText(this@LoginActivity, "이름 가져오기 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("LoginActivity", "Response Error: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "이름 가져오기 실패: 서버 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NameResponse>, t: Throwable) {
                Log.e("LoginActivity", "Network Error: ${t.message}")
                Toast.makeText(this@LoginActivity, "이름 가져오기 실패: 네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })

        viewPasswordIv.setOnClickListener {
            passwordET.transformationMethod = HideReturnsTransformationMethod.getInstance()
            viewPasswordIv.visibility = ImageView.GONE
            hidePasswordIv.visibility = ImageView.VISIBLE
        }

        hidePasswordIv.setOnClickListener {
            passwordET.transformationMethod = PasswordTransformationMethod.getInstance()
            viewPasswordIv.visibility = ImageView.VISIBLE
            hidePasswordIv.visibility = ImageView.GONE
        }


    }
}
