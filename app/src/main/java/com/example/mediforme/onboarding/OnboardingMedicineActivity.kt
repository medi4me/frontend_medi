package com.example.mediforme.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivityOnboardingMedicineBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class OnboardingMedicineActivity : AppCompatActivity(), SearchResultAdapter.OnItemClickListener {
    lateinit var binding: ActivityOnboardingMedicineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            startActivity(Intent(this, OnboardingDetailActivity::class.java))
        }

        binding.searchMedicineIv.setOnClickListener {
            showSearchResultsBottomSheet()
        }

        binding.skippingTv.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun showSearchResultsBottomSheet() {
        val bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_search_results, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        val recyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.recyclerView)
        if (recyclerView == null) {
            Log.e("OnboardingMedicineActivity", "RecyclerView not found in bottom sheet layout")
            return
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchResults = listOf(
            searchResults(
                imageResId = R.drawable.ic_tylenol,
                name = "타이레놀정 500mg"),
            searchResults(
                imageResId = R.drawable.ic_tylenol,
                name = "우먼스타이레놀정"),
            searchResults(
                imageResId = R.drawable.ic_tylenol,
                name = "어린이 타이레놀")
        )
        val adapter = SearchResultAdapter(searchResults, this)
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
