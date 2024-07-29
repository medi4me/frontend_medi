package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediforme.Join_phone
import com.example.mediforme.R

class Join_service : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_service)

        val veriBtn : Button = findViewById(R.id.veri_btn)

        veriBtn.setOnClickListener {
            val intent = Intent(this@Join_service, Join_phone::class.java)
            startActivity(intent)
        }

    }
}