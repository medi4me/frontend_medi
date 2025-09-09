package com.example.mediforme.ui.search

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.databinding.FragmentBottomSheetBinding
import android.net.Uri
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.CameraMedicineResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class AddMedicineActivity : AppCompatActivity() {
    private lateinit var binding: FragmentBottomSheetBinding

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Intent로부터 사진 파일 URI를 받아옴
        val photoUriString = intent.getStringExtra("photoUri")
        val photoUri = Uri.parse(photoUriString)

        // 서버로 사진 파일을 전송하고 받아온 데이터를 리사이클러뷰에 표시
        uploadPhotoAndDisplayResults(photoUri)

        binding.addMedicineButton.setOnClickListener {
            Toast.makeText(this, "버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun uploadPhotoAndDisplayResults(photoUri: Uri) {
        val photoFile = File(photoUri.path)
        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), photoFile)
        val body = MultipartBody.Part.createFormData("file", photoFile.name, requestFile)

        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<List<CameraMedicineResponse>>> = apiService.uploadImage(body)
                if (response.isSuccessful) {
                    val medicineResponses = response.body()?.result ?: emptyList()
                    val medicines = medicineResponses.map { response ->
                        Medicine(
                            name = response.name,
                            dosage = ""
                        )
                    }
                    val adapter = MedicineAdapter(
                        this@AddMedicineActivity,
                        medicines
                    ) { medicine ->
                        showAddMedicineActivity(medicine)
                    }
                    binding.medicineRecyclerView.layoutManager = LinearLayoutManager(this@AddMedicineActivity)
                    binding.medicineRecyclerView.adapter = adapter
                } else {
                    Log.e("AddMedicineActivity", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("AddMedicineActivity", "Request failed", e)
            }
        }
    }

    private fun showAddMedicineActivity(medicine: com.example.mediforme.ui.search.Medicine) {
        val intent = Intent(this, com.example.mediforme.ui.search.AddMedicineResultActivity::class.java).apply {
            putExtra("medicine_name", medicine.name)
            putExtra("medicine_dosage", medicine.dosage)
        }
        startActivity(intent)
    }
}

