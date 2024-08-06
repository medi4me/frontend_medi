package com.example.mediforme.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mediforme.R

class SearchIDFragment : Fragment() {

    private lateinit var phoneNumET: EditText
    private lateinit var veriET: EditText
    private lateinit var veriSendBtn: Button
    private lateinit var enterBtn: Button
    private lateinit var searchIdBtn: Button

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
}