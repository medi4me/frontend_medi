package com.example.mediforme.search

import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.content.ContentResolver
import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.Data.CameraMedicineResponse
import com.example.mediforme.Data.CameraService
import com.example.mediforme.Data.MedicineResponse
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivitySearchresultBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class SearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchresultBinding

    // Define request codes
    private val REQUEST_PERMISSIONS = 1
    private val REQUEST_IMAGE_CAPTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchresultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Check for permissions and set up listeners
        checkPermissions()

        uploadDrawableImage()

        //이미지 뷰에 클릭 리스너 달고, 사진찍게 함
        binding.medicineIv.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSIONS)
            }
        }

        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        imageBitmap?.let {
            binding.medicineIv.setImageBitmap(it)
        }


        // 복용 약에 추가하기 버튼을 눌렀을 시
        val bottomSheetFragment = BottomSheetFragment()
        binding.addToMedsButton.setOnClickListener {
            //bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
            startActivity(Intent(this, AddMedicineActivity::class.java))
        }

        // 정보 더 알아보기 버튼 눌렀을 시
        val bottomSheetFragment2 = BottomSheetFragment2()
        binding.moreInfoTv.setOnClickListener {
            bottomSheetFragment2.show(supportFragmentManager, "BottomSheetDialog2")
        }

        // 약물 조합 확인하기 버튼 눌렀을 시
        val bottomSheetFragment3 = BottomSheetFragment3()
        binding.checkCombinationButton.setOnClickListener {
            bottomSheetFragment3.show(supportFragmentManager, "BottomSheetDialog2")
        }

        binding.backButton.setOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 프래그먼트로 돌아감
            onBackPressed()
        }
    }

    private fun uploadDrawableImage() {
        // drawable 리소스에서 이미지 파일을 가져오기
        val inputStream: InputStream = resources.openRawResource(R.drawable.tairenol)
        val file = File(cacheDir, "tirenol.png")
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // Retrofit 인스턴스 생성 및 요청 실행
        val retrofit = getRetrofit() // getRetrofit()은 Retrofit 인스턴스를 반환하는 메서드입니다.
        val service = retrofit.create(CameraService::class.java)
        val call = service.uploadImage(body)

        call.enqueue(object : Callback<List<CameraMedicineResponse>> {
            override fun onResponse(call: Call<List<CameraMedicineResponse>>, response: Response<List<CameraMedicineResponse>>) {
                if (response.isSuccessful) {
                    val cameraMedicineResponses = response.body()

                    // 서버에서 받은 데이터를 MedicineList로 변환
                    val medicines = cameraMedicineResponses?.map { response ->
                        MedicineList(
                            imageResId = response.imageUrl,
                            name = response.name,
                            dosage = response.dosage ?: "",
                            effects = response.benefit ?: "",
                            howToEat = response.drugInteraction ?: ""
                        )
                    } ?: emptyList()

                    // RecyclerView 어댑터에 데이터 설정
                    val listAdapter = MedicineListAdapter(medicines)
                    binding.medicineInfoRecyclerView.adapter = listAdapter
                } else {
                    Log.e("SearchResultActivity", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<CameraMedicineResponse>>, t: Throwable) {
                Log.e("SearchResultActivity", "Request failed", t)
            }
        })
    }

    private fun checkPermissions() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        if (permissions.any { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS)
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                dispatchTakePictureIntent()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri == null) {
                // If there's no data (might happen on some devices), try getting the image from the intent
                val extras = data.extras
                val imageBitmap = extras?.get("data") as? Bitmap
                binding.medicineIv.setImageBitmap(imageBitmap)
            } else {
                binding.medicineIv.setImageURI(imageUri)
            }
        }
    }
}
