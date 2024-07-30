package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Join_id : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_id)

        val nextBtn: Button = findViewById(R.id.next_btn)

        nextBtn.setOnClickListener {
            val intent = Intent(this@Join_id, Join_password::class.java)
            startActivity(intent)
        }
    }
}