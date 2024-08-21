package com.example.mediforme.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.Data.MedicineApiService
import com.example.mediforme.Data.MedicineResponse
import com.example.mediforme.Data.Medicines
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivityOnboardingMedicineBinding
import com.example.mediforme.search.CameraActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnboardingMedicineActivity : AppCompatActivity(), SearchResultAdapter.OnItemClickListener {

    private lateinit var binding: ActivityOnboardingMedicineBinding
    private val apiService = getRetrofit().create(MedicineApiService::class.java)

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

        // RecyclerView에 더미 데이터 설정하기
        val dummyDatas = listOf(
            SearchAddResult("타이레놀정 슈퍼우먼 플러스 울트라 500mg", "14 : 20 / 식후 / 2정"),
            SearchAddResult("우먼스타이레놀정", "18 : 20 / 식전 / 1정"),
            SearchAddResult("어린이 타이레놀", "21 : 20 / 식후 / 3정"),
            SearchAddResult("타이레놀정 500mg", "14 : 20 / 식후 / 2정"),
            SearchAddResult("우먼스타이레놀정", "18 : 20 / 식전 / 1정"),
            SearchAddResult("어린이 타이레놀", "21 : 20 / 식후 / 3정")
        )

        val searchAddResultAdapter = SearchAddResultAdapter(dummyDatas)
        binding.searchAddResultsRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.searchAddResultsRecyclerview.adapter = searchAddResultAdapter

        // 검색 아이콘 클릭 리스너 설정
        binding.searchMedicineIv.setOnClickListener {
            val query = binding.medicineNameEV.text.toString()
            if (query.isNotEmpty()) {
                fetchMedicinesFromServer(query)
            }
        }
    }

    private fun fetchMedicinesFromServer(query: String) {
        val apiService = getRetrofit().create(MedicineApiService::class.java)
        val call = apiService.getMedicines(query)

        call.enqueue(object : Callback<MedicineResponse> {
            override fun onResponse(call: Call<MedicineResponse>, response: Response<MedicineResponse>) {
                if (response.isSuccessful) {
                    val medicines = response.body()?.medicines ?: emptyList()
                    // BottomSheetDialog에 데이터 전달
                    showSearchResultsBottomSheet(medicines)
                } else {
                    Log.e("OnboardingMedicineActivity", "Response error")
                }
            }

            override fun onFailure(call: Call<MedicineResponse>, t: Throwable) {
                Log.e("OnboardingMedicineActivity", "Fetch error", t)
            }
        })
    }

    private fun showSearchResultsBottomSheet(medicines: List<Medicines>) {
        val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_search_results, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        val recyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView == null) {
            Log.e("OnboardingMedicineActivity", "RecyclerView not found in bottom sheet layout")
            return
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = SearchResultAdapter(medicines, this)
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
