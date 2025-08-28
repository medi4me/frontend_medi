package com.example.mediforme.ui.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.databinding.ActivitySearchresultBinding
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
import java.io.FileOutputStream
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class SearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchresultBinding
    private var selectedMedicineName: String? = null

    private val REQUEST_PERMISSIONS = 1
    private val REQUEST_IMAGE_CAPTURE = 2

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchresultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check for permissions and set up listeners
        checkPermissions()

        val imageBitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        imageBitmap?.let {
            binding.medicineIv.setImageBitmap(it)

            // Save the bitmap as a PNG file and upload it
            val pngFile = saveBitmapAsPng(it)
            uploadImageToServer(pngFile)
        }

        binding.medicineIv.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSIONS)
            }
        }

        binding.addToMedsButton.setOnClickListener {
            val intent = Intent(this, com.example.mediforme.ui.search.AddMedicineActivity::class.java)
            val fileUri = Uri.fromFile(File(cacheDir, "captured_image.png"))
            intent.putExtra("photoUri", fileUri.toString())
            startActivity(intent)
        }

        binding.moreInfoTv.setOnClickListener {
            selectedMedicineName?.let { medicineNames ->
                val bottomSheetFragment2 = BottomSheetFragment2()
                val bundle = Bundle()
                bundle.putStringArrayList("medicine_names", arrayListOf(medicineNames))
                bottomSheetFragment2.arguments = bundle
                bottomSheetFragment2.show(supportFragmentManager, "BottomSheetDialog2")
            }
        }

        binding.checkCombinationButton.setOnClickListener {
            val bottomSheetFragment3 = BottomSheetFragment3()
            val fileUri = Uri.fromFile(File(cacheDir, "captured_image.png"))
            val bundle = Bundle()
            bundle.putString("photoUri", fileUri.toString())
            bottomSheetFragment3.arguments = bundle
            bottomSheetFragment3.show(supportFragmentManager, "BottomSheetDialog3")
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
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
            val imageBitmap: Bitmap? = data.extras?.get("data") as? Bitmap

            imageBitmap?.let {
                binding.medicineIv.setImageBitmap(it)

                // Save the bitmap as a PNG file and upload it
                val pngFile = saveBitmapAsPng(it)
                uploadImageToServer(pngFile)
            }
        }
    }

    private fun saveBitmapAsPng(bitmap: Bitmap): File {
        val pngFile = File(cacheDir, "captured_image.png")
        FileOutputStream(pngFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return pngFile
    }

    private fun uploadImageToServer(file: File) {
        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<List<CameraMedicineResponse>>> = apiService.uploadImage(body)
                if (response.isSuccessful) {
                    val cameraMedicineResponses = response.body()?.result ?: emptyList()

                    val medicines = cameraMedicineResponses.map { response ->
                        MedicineList(
                            imageResId = response.imageUrl,
                            name = response.name,
                            dosage = response.dosage ?: "",
                            effects = response.benefit ?: "",
                            howToEat = response.dosage ?: ""
                        )
                    }

                    selectedMedicineName = cameraMedicineResponses.firstOrNull {
                        it.name.contains("타이레놀") ||
                                it.name.contains("초당아스피린장용정") ||
                                it.name.contains("페니라민정")
                    }?.name

                    val listAdapter = MedicineListAdapter(medicines)
                    binding.medicineInfoRecyclerView.layoutManager = LinearLayoutManager(this@SearchResultActivity)
                    binding.medicineInfoRecyclerView.adapter = listAdapter
                } else {
                    Toast.makeText(this@SearchResultActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SearchResultActivity, "Request failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
