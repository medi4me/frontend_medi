package com.example.mediforme.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.JoinServiceActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var passwordET: EditText
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewPasswordIv: ImageView
    private lateinit var hidePasswordIv: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // View Binding 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
