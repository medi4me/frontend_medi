package com.example.mediforme.search

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentBottomSheetBinding

class AddMedicineActivity : AppCompatActivity() {
    private lateinit var binding: FragmentBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val dummyData = listOf(
            Medicine("부타정", "0.7mg"),
            Medicine("파프티정", "0.5mg"),
            Medicine("타이레놀", "0.3mg"),
            Medicine("부타정", "0.7mg")
        )

        // 어댑터 설정
        val adapter = MedicineAdapter(this, dummyData) { medicine ->
            showAddMedicineActivity(medicine) // 클릭 시 호출될 콜백
        }
        binding.medicineRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.medicineRecyclerView.adapter = adapter

        binding.addMedicineButton.setOnClickListener {
            Toast.makeText(this, "버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        binding.backButton.setOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 프래그먼트로 돌아감
            onBackPressed()
        }
    }

    private fun showAddMedicineActivity(medicine: Medicine) {
        val intent = Intent(this, AddMedicineResultActivity::class.java).apply {
            putExtra("medicine_name", medicine.name)
            putExtra("medicine_dosage", medicine.dosage)
        }
        startActivity(intent)
    }
}
