package com.example.mediforme.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.ui.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivityCheckMedicineBinding
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.MedicineResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class CheckMedicineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckMedicineBinding

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckMedicineBinding.inflate(layoutInflater)
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
            // Add functionality for verification button here
        }

        binding.searchMedicineIv.setOnClickListener {
            showSearchResultsBottomSheet()
        }

        binding.skippingTv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("START_FRAGMENT", "SEARCH")
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.searchWithCameraTv.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showSearchResultsBottomSheet() {
        val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_search_results, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        val recyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView == null) {
            Log.e("CheckMedicineActivity", "RecyclerView not found in bottom sheet layout")
            return
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchQuery = binding.medicineNameEV.text.toString()

        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<MedicineResponse>> = apiService.getMedicines(searchQuery)
                if (response.isSuccessful) {
                    val dataMedicines = response.body()?.result?.medicines ?: emptyList()
                    val adapter = SearchWithNameAdapter(dataMedicines)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("CheckMedicineActivity", "Failed to get medicines: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CheckMedicineActivity", "API call failed", e)
            }
        }

        bottomSheetDialog.show()
    }
}
