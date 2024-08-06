package com.example.mediforme.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivitySearchresultBinding
import com.example.mediforme.onboarding.OnboardingMedicineActivity

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

        // 복용 약에 추가하기 버튼을 눌렀을 시
        val bottomSheetFragment = BottomSheetFragment()
        binding.addToMedsButton.setOnClickListener {
            //bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
            startActivity(Intent(this, AddMedicineActivity::class.java))
        }

        // 정보 더 알아보기 버튼 눌렀을 시
        val bottomSheetFragment2 = BottomSheetFragment2()
        binding.moreInfoTv.setOnClickListener {
            bottomSheetFragment2.show(supportFragmentManager, "BottomSheetDialog2")
        }

        // 약물 조합 확인하기 버튼 눌렀을 시
        val bottomSheetFragment3 = BottomSheetFragment3()
        binding.checkCombinationButton.setOnClickListener {
            bottomSheetFragment3.show(supportFragmentManager, "BottomSheetDialog2")
        }

        binding.backButton.setOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 프래그먼트로 돌아감
            onBackPressed()
        }
    }
}
