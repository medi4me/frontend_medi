package com.example.mediforme.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.databinding.ActivitySearchresultBinding

class SearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchresultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchresultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetFragment = BottomSheetFragment()
        binding.addToMedsButton.setOnClickListener {
            bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
        }

        binding.backButton.setOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 프래그먼트로 돌아감
            onBackPressed()
        }
    }
}
