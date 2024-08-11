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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.ActivitySearchresultBinding

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

        //이미지 뷰에 클릭 리스너 달고, 사진찍게 함
        binding.medicineIv.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSIONS)
            }
        }

        // 더미 데이터 생성
        val dummyData = listOf(
            MedicineList(
                imageResId = R.drawable.medicine_ex,
                name = "부타정",
                dosage = "0.7mg",
                effects = "이 약의 효능입니다.",
                howToEat = "하루 2번 / 3정 이하"
            ),
            MedicineList(
                imageResId = R.drawable.medicine_ex,
                name = "파프티정",
                dosage = "1.0mg",
                effects = "부작용이 적습니다.",
                howToEat = "하루 1번 / 2정 이하"
            ),
            MedicineList(
                imageResId = R.drawable.medicine_ex,
                name = "테스민정",
                dosage = "0.5mg",
                effects = "효능이 좋습니다.",
                howToEat = "하루 3번 / 1정 이하"
            )
        )


        val listAdapter = MedicineListAdapter(dummyData)
        binding.medicineInfoRecyclerView.adapter = listAdapter

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
