package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class JoinIdActivity : AppCompatActivity() {
    private lateinit var user_id_ET: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_id)

        //이전 화면에서 받아온 인텐트
        val phoneNumber = intent.getStringExtra("user_phoneNumber")

        user_id_ET = findViewById(R.id.user_id_ET)


        val nextBtn: Button = findViewById(R.id.next_btn)

        nextBtn.setOnClickListener {
            val user_id = user_id_ET.text.toString().trim()

            val intent = Intent(this@JoinIdActivity, JoinPasswordActivity::class.java).apply {
                putExtra("user_id",user_id)
                putExtra("user_phoneNumber",phoneNumber)
                putExtra("consent","AGREE")
            }
            startActivity(intent)
        }
    }
}

