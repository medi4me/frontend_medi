package com.example.mediforme.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mediforme.R

class InfoPWDFragment : Fragment() {

    private lateinit var enterBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_infopwd, container, false)

        enterBtn = view.findViewById(R.id.enter_Btn)

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