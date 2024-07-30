package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class JoinServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_service)

        val veriBtn : Button = findViewById(R.id.veri_btn)

        veriBtn.setOnClickListener {
            val intent = Intent(this@JoinServiceActivity, JoinPhoneActivity::class.java)
            startActivity(intent)
        }

    }
}