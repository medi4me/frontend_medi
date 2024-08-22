package com.example.mediforme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.timer

class JoinVericodeActivity : AppCompatActivity() {

    private var second = 0
    private var minute = 0
    private var timeTick = 300 // 제한시간 5분을 300초로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join_vericode)

        setTimer()

        val veri_btn: Button = findViewById(R.id.veri_btn)

        val reveri_TV: TextView = findViewById(R.id.reveri_TV)

        reveri_TV.setOnClickListener { finish() }

        veri_btn.setOnClickListener {
            val intent = Intent(this@JoinVericodeActivity, JoinIdActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setTimer() {
        second = timeTick % 60
        minute = timeTick / 60
        val textView: TextView = findViewById(R.id.timer_TV)

        timer(period = 1000, initialDelay = 1000) {
            runOnUiThread {
                textView.text = String.format("0%d : %02d", minute, second)
                if (second == 0) {
                    if (minute == 0) {
                        cancel() // 타이머 종료
                    } else {
                        minute--
                        second = 60
                    }
                }
                second--
            }
        }
    }
}
