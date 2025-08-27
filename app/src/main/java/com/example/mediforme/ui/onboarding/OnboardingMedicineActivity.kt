package com.example.mediforme.ui.onboarding

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.ui.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivityOnboardingMedicineBinding
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.MedicineResponse
import com.example.mediforme.remote.model.response.Medicines
import com.example.mediforme.ui.login.LoginActivity
import com.example.mediforme.ui.search.CameraActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class OnboardingMedicineActivity : AppCompatActivity(), SearchResultAdapter.OnItemClickListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityOnboardingMedicineBinding
    private var accessToken: String? = null

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Fetching the token from SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        accessToken = sharedPreferences.getString("accessToken", null)

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

//        // RecyclerView에 더미 데이터 설정하기
//        val dummyDatas = listOf(
//            SearchAddResult("타이레놀정 슈퍼우먼 플러스 울트라 500mg", "14 : 20 / 식후 / 2정"),
//            SearchAddResult("우먼스타이레놀정", "18 : 20 / 식전 / 1정"),
//            SearchAddResult("어린이 타이레놀", "21 : 20 / 식후 / 3정"),
//            SearchAddResult("타이레놀정 500mg", "14 : 20 / 식후 / 2정"),
//            SearchAddResult("우먼스타이레놀정", "18 : 20 / 식전 / 1정"),
//            SearchAddResult("어린이 타이레놀", "21 : 20 / 식후 / 3정")
//        )
//
//        val searchAddResultAdapter = SearchAddResultAdapter(dummyDatas)
//        binding.searchAddResultsRecyclerview.layoutManager = LinearLayoutManager(this)
//        binding.searchAddResultsRecyclerview.adapter = searchAddResultAdapter

        // 검색 아이콘 클릭 리스너 설정
        binding.searchMedicineIv.setOnClickListener {
            val query = binding.medicineNameEV.text.toString()
            if (query.isNotEmpty()) {
                fetchMedicinesFromServer(query)
            }
        }

        // 서버로부터 데이터를 받아와 RecyclerView에 설정하기
        fetchMedicinesInfoFromServer()
    }

    private fun fetchMedicinesFromServer(query: String) {
        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<MedicineResponse>> = apiService.getMedicines(query)
                if (response.isSuccessful) {
                    val medicines = response.body()?.result?.medicines ?: emptyList()

                    // BottomSheetDialog에 데이터 전달
                    showSearchResultsBottomSheet(medicines)
                } else {
                    Log.e("OnboardingMedicineActivity", "Response error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("OnboardingMedicineActivity", "Fetch error", e)
            }
        }
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

    private fun fetchMedicinesInfoFromServer() {
        val token = "Bearer $accessToken"
        if (accessToken.isNullOrEmpty()) {
            handleUnauthorized()
            return
        }

        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<MedicineResponse>> = apiService.getUserMedicines(token)
                Log.d("OnboardingMedicineActivity", "Response received: ${response.code()}")
                if (response.isSuccessful) {
                    val medicines = response.body()?.result?.medicines ?: emptyList()
                    setupRecyclerView(medicines)
                } else {
                    Log.e("OnboardingMedicineActivity", "Response error: ${response.code()} ${response.message()}")
                    Log.e("OnboardingMedicineActivity", "Response body: ${response.errorBody()?.string()}")
                    if (response.code() == 401) {
                        handleUnauthorized()
                    }
                }
            } catch (e: Exception) {
                Log.e("OnboardingMedicineActivity", "Fetch error", e)
            }
        }
    }

    private fun handleUnauthorized() {
        // 토큰이 만료되었거나 유효하지 않을 때 처리하는 로직 추가
        // 예: 사용자를 로그인 화면으로 보내기
        Toast.makeText(this, "인증이 만료되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupRecyclerView(medicines: List<Medicines>) {
        val results = medicines.map { medicine ->
            SearchAddResult(
                image = medicine.itemImage ?: "",
                name = medicine.itemName,
                contents = "${medicine.time ?: "No time info"} / ${medicine.meal ?: "No meal info"} / ${medicine.dosage ?: "No dosage info"}"
            )
        }

        val searchAddResultAdapter = SearchAddResultAdapter(results)
        binding.searchAddResultsRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.searchAddResultsRecyclerview.adapter = searchAddResultAdapter
    }

    override fun onItemClick(name: String) {
        val intent = Intent(this, OnboardingDetailActivity::class.java).apply {
            putExtra("medicine_name", name)
        }
        startActivity(intent)
    }
}
