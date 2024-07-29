package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Join_phone : AppCompatActivity() {

    private lateinit var phone_num_ET: EditText
    private lateinit var veri_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_phone)

        phone_num_ET = findViewById(R.id.phone_num_ET)
        veri_btn = findViewById(R.id.veri_btn)

        veri_btn.isEnabled = false


        phone_num_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val message = phone_num_ET.text.toString()
                veri_btn.isEnabled = message.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        veri_btn.setOnClickListener {
            val intent = Intent(this@Join_phone, Join_vericode::class.java)
            startActivity(intent)
        }

    }
}