package com.example.mediforme.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivityOnboardingMedicineBinding
import com.example.mediforme.search.CameraActivity
import com.example.mediforme.search.MedicineList
import com.example.mediforme.search.MedicineListAdapter
import com.example.mediforme.search.SearchResultActivity

import com.google.android.material.bottomsheet.BottomSheetDialog

class OnboardingMedicineActivity : AppCompatActivity(), SearchResultAdapter.OnItemClickListener {

    private lateinit var binding: ActivityOnboardingMedicineBinding
    private lateinit var adapter: SearchResultAdapter
    private val searchResults = mutableListOf<SearchResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.veriBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.skippingTv.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.searchWithCameraTv.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        // 더미 데이터 생성
        // RecyclerView에 더미 데이터 설정하기
        val dummyDatas = listOf(
            SearchAddResult("타이레놀정 슈퍼우먼 플러스 울트라 500mg", "14 : 20 / 식후 / 2정"),
            SearchAddResult("우먼스타이레놀정", "18 : 20 / 식전 / 1정"),
            SearchAddResult("어린이 타이레놀", "21 : 20 / 식후 / 3정"),
            SearchAddResult("타이레놀정 500mg", "14 : 20 / 식후 / 2정"),
            SearchAddResult("우먼스타이레놀정", "18 : 20 / 식전 / 1정"),
            SearchAddResult("어린이 타이레놀", "21 : 20 / 식후 / 3정")
        )

        val adapters = SearchAddResultAdapter(dummyDatas)
        binding.searchAddResultsRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.searchAddResultsRecyclerview.adapter = adapters

        // 더미 데이터
        val dummyData = listOf(
            SearchResult(R.drawable.ic_tylenol, "타이레놀정 슈퍼우먼 플러스 울트라 500mg"),
            SearchResult(R.drawable.ic_tylenol, "우먼스타이레놀정"),
            SearchResult(R.drawable.ic_tylenol, "어린이 타이레놀"),
            SearchResult(R.drawable.ic_tylenol, "아스피린정 100mg"),
            SearchResult(R.drawable.ic_tylenol, "애드빌 200mg"),
            SearchResult(R.drawable.ic_tylenol, "모트린 400mg"),
            SearchResult(R.drawable.ic_tylenol, "알리브 250mg"),
            SearchResult(R.drawable.ic_tylenol, "페니실린 500mg"),
            SearchResult(R.drawable.ic_tylenol, "사이타멜 500mg"),
            SearchResult(R.drawable.ic_tylenol, "엑세드린 500mg"),
            SearchResult(R.drawable.ic_tylenol, "부루펜 200mg"),
            SearchResult(R.drawable.ic_tylenol, "모터릴 400mg"),
            SearchResult(R.drawable.ic_tylenol, "덴트렉스 500mg"),
            SearchResult(R.drawable.ic_tylenol, "로펜 600mg"),
            SearchResult(R.drawable.ic_tylenol, "스펙트린 500mg"),
            SearchResult(R.drawable.ic_tylenol, "다이조날 650mg")

        )

        searchResults.addAll(dummyData)

        adapter = SearchResultAdapter(searchResults, this)

        // 검색 아이콘 클릭 리스너 설정
        binding.searchMedicineIv.setOnClickListener {
            val query = binding.medicineNameEV.text.toString()
            if (query.isNotEmpty()) {
                showSearchResultsBottomSheet(query)
            }
        }
    }

    private fun showSearchResultsBottomSheet(query: String) {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_search_results, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        val recyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView == null) {
            Log.e("OnboardingMedicineActivity", "RecyclerView not found in bottom sheet layout")
            return
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        // 필터링된 결과 리스트 생성
        val filteredList = searchResults.filter {
            it.name.contains(query, ignoreCase = true)
        }
        val adapter = SearchResultAdapter(filteredList, this)
        recyclerView.adapter = adapter

        bottomSheetDialog.show()
    }

    override fun onItemClick(name: String) {
        val intent = Intent(this, OnboardingDetailActivity::class.java).apply {
            putExtra("medicine_name", name)
        }
        startActivity(intent)
    }
}
