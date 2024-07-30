package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class JoinPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_password)

        val nextBtn: Button = findViewById(R.id.next_btn)

        nextBtn.setOnClickListener {
            val intent = Intent(this@JoinPasswordActivity, JoinNameActivity::class.java)
            startActivity(intent)
        }
    }
}