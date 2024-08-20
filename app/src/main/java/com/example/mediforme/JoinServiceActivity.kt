import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.Data.ApiService
import com.example.mediforme.Data.ConsentRequest
import com.example.mediforme.Data.ServerResponse
import com.example.mediforme.JoinPhoneActivity
import com.example.mediforme.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinServiceActivity : AppCompatActivity() {

    private lateinit var checkBox: CheckBox
    private lateinit var smallCheck1: CheckBox
    private lateinit var smallCheck2: CheckBox
    private lateinit var smallCheck3: CheckBox
    private lateinit var smallCheck4: CheckBox
    private lateinit var veriBtn: Button
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_service)

        // Retrofit 인스턴스 생성
        retrofit = Retrofit.Builder()
            .baseUrl("https://your-api-base-url.com") // 실제 API의 Base URL로 변경해야 합니다.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // ApiService 인스턴스 생성
        apiService = retrofit.create(ApiService::class.java)

        veriBtn = findViewById(R.id.veri_btn)
        checkBox = findViewById(R.id.check_box)
        smallCheck1 = findViewById(R.id.small_check1)
        smallCheck2 = findViewById(R.id.small_check2)
        smallCheck3 = findViewById(R.id.small_check3)
        smallCheck4 = findViewById(R.id.small_check4)

        setupCheckBoxListeners()
        updateButtonState()  // 초기 상태 설정

        veriBtn.setOnClickListener {
            if (smallCheck1.isChecked && smallCheck2.isChecked && smallCheck3.isChecked) {
                // 동의한 경우 서버로 데이터 전송
                val consentRequest = ConsentRequest(
                    name = "User's Name",  // 실제 사용자 이름으로 교체
                    password = "User's Password",  // 실제 사용자 비밀번호로 교체
                    phone = "User's Phone Number",  // 실제 사용자 휴대폰 번호로 교체
                    memberID = "User's Member ID" // 실제 사용자 ID로 교체
                )

                apiService.postConsent(consentRequest).enqueue(object : Callback<ServerResponse> {
                    override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                        if (response.isSuccessful) {
                            val serverResponse = response.body()
                            if (serverResponse?.isSuccess == true) {
                                // 서버로부터 성공적인 응답을 받은 경우
                                val intent = Intent(this@JoinServiceActivity, JoinPhoneActivity::class.java)
                                intent.putExtra("consent", "AGREE")
                                startActivity(intent)
                            } else {
                                // 서버로부터 실패 응답을 받은 경우 처리
                                Toast.makeText(this@JoinServiceActivity, "서버 응답 오류: ${serverResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // 서버 오류 처리
                            Toast.makeText(this@JoinServiceActivity, "서버 오류: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                        // 네트워크 오류 처리
                        Toast.makeText(this@JoinServiceActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "필수 약관에 모두 동의해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCheckBoxListeners() {
        // 전체 동의 체크박스 클릭 리스너
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            smallCheck1.isChecked = isChecked
            smallCheck2.isChecked = isChecked
            smallCheck3.isChecked = isChecked
            smallCheck4.isChecked = isChecked
            updateButtonState()
        }

        val smallCheckBoxes = listOf(smallCheck1, smallCheck2, smallCheck3, smallCheck4)
        smallCheckBoxes.forEach { smallCheckBox ->
            smallCheckBox.setOnCheckedChangeListener { _, _ ->
                updateMainCheckBox()
                updateButtonState()
            }
        }
    }

    private fun updateMainCheckBox() {
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = smallCheck1.isChecked && smallCheck2.isChecked && smallCheck3.isChecked && smallCheck4.isChecked
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            smallCheck1.isChecked = isChecked
            smallCheck2.isChecked = isChecked
            smallCheck3.isChecked = isChecked
            smallCheck4.isChecked = isChecked
            updateButtonState()
        }
    }

    private fun updateButtonState() {
        veriBtn.isEnabled = smallCheck1.isChecked && smallCheck2.isChecked && smallCheck3.isChecked
    }
}



//package com.example.mediforme
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.CheckBox
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//
//class JoinServiceActivity : AppCompatActivity() {
//
//    private lateinit var checkBox: CheckBox
//    private lateinit var smallCheck1: CheckBox
//    private lateinit var smallCheck2: CheckBox
//    private lateinit var smallCheck3: CheckBox
//    private lateinit var smallCheck4: CheckBox
//    private lateinit var veriBtn: Button
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_join_service)
//
//        veriBtn = findViewById(R.id.veri_btn)
//
//        veriBtn.setOnClickListener {
//            if (smallCheck1.isChecked && smallCheck2.isChecked && smallCheck3.isChecked) {
//                val intent = Intent(this@JoinServiceActivity, JoinPhoneActivity::class.java)
//                intent.putExtra("consent","AGREE")
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "필수 약관에 모두 동의해주세요.", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        checkBox = findViewById(R.id.check_box)
//        smallCheck1 = findViewById(R.id.small_check1)
//        smallCheck2 = findViewById(R.id.small_check2)
//        smallCheck3 = findViewById(R.id.small_check3)
//        smallCheck4 = findViewById(R.id.small_check4)
//
//        setupCheckBoxListeners()
//        updateButtonState()  // 초기 상태 설정
//
//    }
//
//    private fun setupCheckBoxListeners() {
//        // 전체 동의 체크박스 클릭 리스너
//        checkBox.setOnCheckedChangeListener { _, isChecked ->
//            smallCheck1.isChecked = isChecked
//            smallCheck2.isChecked = isChecked
//            smallCheck3.isChecked = isChecked
//            smallCheck4.isChecked = isChecked
//            updateButtonState()
//        }
//
//        val smallCheckBoxes = listOf(smallCheck1, smallCheck2, smallCheck3, smallCheck4)
//        smallCheckBoxes.forEach { smallCheckBox ->
//            smallCheckBox.setOnCheckedChangeListener { _, _ ->
//                updateMainCheckBox()
//                updateButtonState()
//            }
//        }
//    }
//
//    private fun updateMainCheckBox() {
//        checkBox.setOnCheckedChangeListener(null)
//        checkBox.isChecked = smallCheck1.isChecked && smallCheck2.isChecked && smallCheck3.isChecked && smallCheck4.isChecked
//        checkBox.setOnCheckedChangeListener { _, isChecked ->
//            smallCheck1.isChecked = isChecked
//            smallCheck2.isChecked = isChecked
//            smallCheck3.isChecked = isChecked
//            smallCheck4.isChecked = isChecked
//            updateButtonState()
//        }
//    }
//
//    private fun updateButtonState() {
//        veriBtn.isEnabled = smallCheck1.isChecked && smallCheck2.isChecked && smallCheck3.isChecked
//    }
//}