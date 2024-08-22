package com.example.mediforme.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mediforme.Data.AuthService
import com.example.mediforme.Data.VerificationRequest
import com.example.mediforme.Data.VerificationResponse
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchIDFragment : Fragment() {

    private lateinit var phoneNumET: EditText
    private lateinit var veriET: EditText
    private lateinit var veriSendBtn: Button
    private lateinit var enterBtn: Button
    private lateinit var searchIdBtn: Button
    private lateinit var authService: AuthService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_searchid, container, false)

        phoneNumET = view.findViewById(R.id.phone_num_ET)
        veriET = view.findViewById(R.id.veri_ET)
        veriSendBtn = view.findViewById(R.id.veri_send_btn)
        enterBtn = view.findViewById(R.id.enter_Btn)
        searchIdBtn = view.findViewById(R.id.saerch_id_Btn)

        authService = getRetrofit().create(AuthService::class.java)

        // 초기 상태에서 버튼을 비활성화합니다.
        veriSendBtn.isEnabled = false
        enterBtn.isEnabled = false
        searchIdBtn.isEnabled = false

        // 버튼의 배경 및 텍스트 색상 리소스를 설정합니다.
        veriSendBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.search_btn_selector)
        enterBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.search_btn_selector)
        searchIdBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_select)

        veriSendBtn.setTextColor(ContextCompat.getColorStateList(requireContext(), R.drawable.search_text_select))
        enterBtn.setTextColor(ContextCompat.getColorStateList(requireContext(), R.drawable.search_text_select))

        phoneNumET.addTextChangedListener(textWatcher)
        veriET.addTextChangedListener(textWatcher)

        veriSendBtn.setOnClickListener {
            sendVerificationCode()
        }

        searchIdBtn.setOnClickListener {
            if (searchIdBtn.isEnabled) {
                // InfoIDFragment로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, InfoIDFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateButtonStates()
        }

        override fun afterTextChanged(s: Editable?) {}

    }

    private fun updateButtonStates() {
        val phoneNumFilled = phoneNumET.text.isNotEmpty()
        val veriFilled = veriET.text.isNotEmpty()

        veriSendBtn.isEnabled = phoneNumFilled
        enterBtn.isEnabled = veriFilled
        searchIdBtn.isEnabled = phoneNumFilled && veriFilled
    }

    private fun sendVerificationCode() {
        val phoneNumber = phoneNumET.text.toString().trim()
        val request = VerificationRequest(phone = phoneNumber)

        authService.sendVerificationCode(request).enqueue(object : Callback<VerificationResponse> {
            override fun onResponse(call: Call<VerificationResponse>, response: Response<VerificationResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    verificationResponse?.let {
                        if (it.isSuccess) {
                            Toast.makeText(requireContext(), "인증 코드가 발송되었습니다.", Toast.LENGTH_SHORT).show()
                        } else if (it.code == "PHONE_NOT_FOUND") {
                            Toast.makeText(requireContext(), "존재하지 않는 번호입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

