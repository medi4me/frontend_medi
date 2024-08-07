package com.example.mediforme.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivitySearchWithNameBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SearchWithNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchWithNameBinding

    // 더미 데이터
    private val allSearchResults = listOf(
        SearchResult(R.drawable.ic_tylenol, "타이레놀정 500mg"),
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchWithNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.veriBtn.isEnabled = false

        binding.medicineNameEV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val message = binding.medicineNameEV.text.toString()
                binding.veriBtn.isEnabled = message.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.veriBtn.setOnClickListener {
            // startActivity(Intent(this, OnboardingDetailActivity::class.java))
        }

        binding.searchMedicineIv.setOnClickListener {
            showSearchResultsBottomSheet()
        }

        binding.skippingTv.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 프래그먼트로 돌아감
            onBackPressed()
        }
    }

    private fun showSearchResultsBottomSheet() {
        val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_search_results, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        val recyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView == null) {
            Log.e("SearchWithNameActivity", "RecyclerView not found in bottom sheet layout")
            return
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        // 현재 EditText에 입력된 검색어 가져오기
        val searchQuery = binding.medicineNameEV.text.toString()

        // 검색어가 비어있으면 빈 리스트를 설정
        val filteredResults = if (searchQuery.isEmpty()) {
            emptyList() // 검색어가 없으면 빈 리스트
        } else {
            allSearchResults.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }

        val adapter = SearchWithNameAdapter(filteredResults)
        recyclerView.adapter = adapter

        bottomSheetDialog.show()
    }

}

data class SearchResult(val imageResId: Int, val name: String)
