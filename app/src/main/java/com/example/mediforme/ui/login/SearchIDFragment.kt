package com.example.mediforme.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mediforme.R
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.request.VerificationRequest
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.FindIDResponse
import com.example.mediforme.remote.model.response.VerificationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class SearchIDFragment : Fragment() {

    private lateinit var phoneNumET: EditText
    private lateinit var veriET: EditText
    private lateinit var veriSendBtn: Button
    private lateinit var enterBtn: Button
    private lateinit var searchIdBtn: Button

    private var memberID: String? = null
    private var password: String? = null

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    @SuppressLint("ResourceType")
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
        enterBtn.setOnClickListener {
            verifyAndFindID()
        }

        searchIdBtn.setOnClickListener {
            if (searchIdBtn.isEnabled) {
                // InfoIDFragment로 이동
                val fragment = InfoIDFragment().apply {
                    arguments = Bundle().apply {
                        putString("memberID", memberID)
                        putString("password", password)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
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
        //searchIdBtn.isEnabled = phoneNumFilled && veriFilled
    }

    private fun sendVerificationCode() {
        val phoneNumber = phoneNumET.text.toString().trim()
        val request = VerificationRequest(phone = phoneNumber)

        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<VerificationResponse>> = apiService.sendVerificationCode(request)
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    verificationResponse?.let {
                        if (it.isSuccess && it.code == "COMMON200") {
                            Toast.makeText(requireContext(), "인증 코드가 발송되었습니다.", Toast.LENGTH_SHORT).show()
                        } else if (it.code == "PHONE_NOT_FOUND") {
                            Toast.makeText(requireContext(), "존재하지 않는 번호입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SearchIDFragment", "네트워크 오류", e)
                Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun verifyAndFindID() {
        val phoneNumber = phoneNumET.text.toString().trim()
        val verificationCode = veriET.text.toString().trim()
        val request = VerificationRequest(phone = phoneNumber, verificationCode = verificationCode)

        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<FindIDResponse>> = apiService.verifyAndFindID(request)
                if (response.isSuccessful) {
                    val findIDResponse = response.body()
                    findIDResponse?.let {
                        if (it.isSuccess && it.code == "COMMON200") {
                            memberID = it.result.result?.memberID
                            password = it.result.result?.password
                            searchIdBtn.isEnabled = true
                        } else {
                            Toast.makeText(requireContext(), "인증에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            searchIdBtn.isEnabled = false
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SearchIDFragment", "네트워크 오류", e)
                Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

