package com.example.mediforme.search

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentBottomSheetBinding
import android.net.Uri
import android.util.Log
import com.example.mediforme.Data.CameraMedicineResponse
import com.example.mediforme.Data.CameraService
import com.example.mediforme.Data.getRetrofit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddMedicineActivity : AppCompatActivity() {
    private lateinit var binding: FragmentBottomSheetBinding

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

        val retrofit = getRetrofit() // Retrofit 인스턴스를 가져오는 함수
        val service = retrofit.create(CameraService::class.java)
        val call = service.uploadImage(body)

        call.enqueue(object : Callback<List<CameraMedicineResponse>> {
            override fun onResponse(call: Call<List<CameraMedicineResponse>>, response: Response<List<CameraMedicineResponse>>) {
                if (response.isSuccessful) {
                    val medicineResponses = response.body()

                    val medicines = medicineResponses?.map { response ->
                        Medicine(
                            name = response.name,
                            dosage = ""
                        )
                    } ?: emptyList()

                    // 어댑터 설정
                    val adapter = MedicineAdapter(this@AddMedicineActivity, medicines) { medicine ->
                        showAddMedicineActivity(medicine)
                    }
                    binding.medicineRecyclerView.layoutManager = LinearLayoutManager(this@AddMedicineActivity)
                    binding.medicineRecyclerView.adapter = adapter

                } else {
                    Log.e("AddMedicineActivity", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<CameraMedicineResponse>>, t: Throwable) {
                Log.e("AddMedicineActivity", "Request failed", t)
            }
        })
    }

    private fun showAddMedicineActivity(medicine: Medicine) {
        val intent = Intent(this, AddMedicineResultActivity::class.java).apply {
            putExtra("medicine_name", medicine.name)
            putExtra("medicine_dosage", medicine.dosage)
        }
        startActivity(intent)
    }
}

