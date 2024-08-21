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
import com.example.mediforme.Data.MedicineApiService
import com.example.mediforme.Data.MedicineResponse
import com.example.mediforme.Data.Medicines
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivitySearchWithNameBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchWithNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchWithNameBinding
    private val apiService by lazy {
        getRetrofit().create(MedicineApiService::class.java)
    }

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
            // Implement verification or other functionality here
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

        val searchQuery = binding.medicineNameEV.text.toString()

        // 서버에서 데이터 호출
        apiService.getMedicines(searchQuery).enqueue(object : Callback<MedicineResponse> {
            override fun onResponse(call: Call<MedicineResponse>, response: Response<MedicineResponse>) {
                if (response.isSuccessful) {
                    val dataMedicines = response.body()?.medicines ?: emptyList()
                    val medicines = dataMedicines.map { DataMedicine ->
                        Medicines(
                            itemName = DataMedicine.itemName,
                            itemImage = DataMedicine.itemImage
                        )
                    }
                    val adapter = SearchWithNameAdapter(medicines)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("SearchWithNameActivity", "Failed to get medicines")
                }
            }

            override fun onFailure(call: Call<MedicineResponse>, t: Throwable) {
                Log.e("SearchWithNameActivity", "API call failed", t)
            }
        })

        bottomSheetDialog.show()
    }

}
