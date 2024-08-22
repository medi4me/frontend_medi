package com.example.mediforme.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mediforme.R

class InfoIDFragment : Fragment() {


    private lateinit var enterBtn: Button
    private lateinit var infoIdTV: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_infoid, container, false)

        enterBtn = view.findViewById(R.id.enter_Btn)
        infoIdTV = view.findViewById(R.id.info_id_TV)

        // Bundle로부터 memberID를 받아와서 TextView에 설정
        val memberID = arguments?.getString("memberID") ?: "ID를 가져올 수 없습니다."
        infoIdTV.text = "아이디 : ${memberID}"

        // Set up the button click listener
        enterBtn.setOnClickListener {
            // Use the activity context to start the LoginActivity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Finish the current activity if necessary
        }

        return view

    }
}