package com.example.mediforme.search

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mediforme.databinding.FragmentSearchWithBackgroundBinding

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchWithBackgroundBinding
    private val REQUEST_PERMISSIONS = 1
    private val REQUEST_IMAGE_CAPTURE = 2

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var shearchNameTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchWithBackgroundBinding.inflate(inflater, container, false)

        // Check permissions when fragment view is created
        checkPermissions()

        sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        shearchNameTV = binding.homeNameTV

        val memberID = sharedPreferences.getString("memberID", "Unknown ID")
        val name = sharedPreferences.getString("name", "홍길동")

        shearchNameTV.text = "$name"


        binding.searchWithCamera.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                dispatchTakePictureIntent()
//            } else {
//                requestCameraPermission()
//            }
            startActivity(Intent(requireContext(), SearchResultActivity::class.java))
        }

        binding.searchWithName.setOnClickListener {
            startActivity(Intent(requireContext(), SearchWithNameActivity::class.java))
        }

        return binding.root
    }

    private fun checkPermissions() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        if (permissions.any { ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED }) {
            requestPermissions(permissions, REQUEST_PERMISSIONS)
        }
    }

    private fun requestCameraPermission() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        requestPermissions(permissions, REQUEST_PERMISSIONS)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
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
            val imageBitmap: Bitmap? = if (imageUri == null) {
                val extras = data.extras
                extras?.get("data") as? Bitmap
            } else {
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
            }

            imageBitmap?.let {
                val intent = Intent(requireContext(), SearchResultActivity::class.java)
                intent.putExtra("imageBitmap", it)
                startActivity(intent)
            }
        }
    }
}
