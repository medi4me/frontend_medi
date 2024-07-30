package com.example.mediforme.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivitySearchresultBinding

class SearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchresultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchresultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 더미 데이터 생성
        val dummyData = listOf(
            MedicineList(
                imageResId = R.drawable.medicine_ex,
                name = "부타정",
                dosage = "0.7mg",
                effects = "이 약의 효능입니다.",
                howToEat = "하루 2번 / 3정 이하"
            ),
            MedicineList(
                imageResId = R.drawable.medicine_ex,
                name = "파프티정",
                dosage = "1.0mg",
                effects = "부작용이 적습니다.",
                howToEat = "하루 1번 / 2정 이하"
            ),
            MedicineList(
                imageResId = R.drawable.medicine_ex,
                name = "테스민정",
                dosage = "0.5mg",
                effects = "효능이 좋습니다.",
                howToEat = "하루 3번 / 1정 이하"
            )
        )


        val listAdapter = MedicineListAdapter(dummyData)
        binding.medicineInfoRecyclerView.adapter = listAdapter

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
