package com.example.mediforme.ui.search

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentBottomSheet3Binding
import com.example.mediforme.databinding.FragmentBottomSheetBinding
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.CameraMedicineResponse
import com.example.mediforme.remote.model.response.MedicineInfoResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class BottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        val dummyData = listOf(
            Medicine("부타정", "0.7mg"),
            Medicine("파프티정", "0.5mg"),
            Medicine("타이레놀", "0.3mg"),
            Medicine("부타정", "0.7mg")
        )

        // 어댑터 설정
        //val adapter = MedicineAdapter(dummyData) { medicine ->
          //  showAddMedicineFragment(medicine) // 클릭 시 호출될 콜백
        //}
        binding.medicineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        //binding.medicineRecyclerView.adapter = adapter

        binding.addMedicineButton.setOnClickListener {
            Toast.makeText(requireActivity(), "버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun showAddMedicineFragment(medicine: Medicine) {
        val addMedicineFragment = AddMedicineFragment(medicine)

        // 현재 BottomSheetFragment의 내부 내용을 교체
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, addMedicineFragment) // R.id.bottom_sheet_container는 교체할 컨테이너의 ID
            .addToBackStack(null) // 백스택에 추가하여 뒤로 가기 가능
            .commit()
    }


    override fun onStart() {
        super.onStart()
        // 최소 높이를 설정합니다.
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            bottomSheet?.minimumHeight = 600 // 최소 높이를 설정합니다. 원하는 dp 값으로 변경하세요.
            // 버튼이 보이도록 설정
            bottomSheet?.viewTreeObserver?.addOnGlobalLayoutListener {
                val parent = bottomSheet.parent as View
                parent.setBackgroundResource(android.R.color.transparent)
            }
        }
    }
}


// BottomSheetFragment2.kt
// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class BottomSheetFragment2 : BottomSheetDialogFragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var tabAdapter: TabAdapter

    // 약 정보를 담을 리스트 (초기화는 나중에 서버 데이터로 대체)
    private var medicineInfoList: List<MedicineInfo> = emptyList()

    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet2, container, false)

        tabLayout = view.findViewById(R.id.tab_layout)
        recyclerView = view.findViewById(R.id.recycler_view)

        // RecyclerView 초기화
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 전달받은 약물 이름을 번들로부터 가져옴
        val medicineNames = arguments?.getStringArrayList("medicine_names") ?: emptyList()

        // 서버에서 데이터를 받아와서 리스트 초기화
        fetchMedicineInfo(medicineNames)

        return view
    }

    private fun fetchMedicineInfo(medicineNames: List<String>) {
        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<List<MedicineInfoResponse>>> = apiService.getMedicineInfo(medicineNames)
                Log.d("BottomSheetFragment2", "Requesting info for: $medicineNames")

                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        apiResponse.result?.let { responseList ->
                            medicineInfoList = responseList.map { response ->
                                MedicineInfo(
                                    title = response.name,
                                    ingredient = response.componentName,
                                    amount = response.amount
                                )
                            }
                            Log.d("BottomSheetFragment2", "Received Medicine Info List: $medicineInfoList")

                            // TabAdapter 설정
                            tabAdapter = TabAdapter(medicineInfoList)
                            recyclerView.adapter = tabAdapter

                            // TabLayout의 탭을 설정
                            tabLayout.removeAllTabs()
                            for (i in medicineInfoList.indices) {
                                tabLayout.addTab(tabLayout.newTab().setText(medicineInfoList[i].title))
                            }
                        }
                    }
                } else {
                    Log.e("BottomSheetFragment2", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("BottomSheetFragment2", "Request failed", e)
            }
        }
    }

    @Parcelize
    data class MedicineInfo(
        val title: String,
        val ingredient: String,
        val amount: String
    ) : Parcelable
}





// BottomSheetFragment3.kt
// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class BottomSheetFragment3 : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheet3Binding

    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheet3Binding.inflate(inflater, container, false)

        val photoUri = arguments?.getString("photoUri")

        // 사진 파일을 서버로 전송
        photoUri?.let {
            val file = File(Uri.parse(it).path)
            val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            uploadPhotoAndDisplayWarnings(body)
        }

//        binding.veriBtnCombination.setOnClickListener {
//            val intent = Intent(requireContext(), CheckMedicineActivity::class.java)
//            startActivity(intent)
//        }

        return binding.root
    }

    private fun uploadPhotoAndDisplayWarnings(body: MultipartBody.Part) {
        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<List<CameraMedicineResponse>>> = apiService.uploadImage(body)
                if (response.isSuccessful) {
                    val responseData = response.body()?.result
                    responseData?.let { data ->
                        if (data.isNotEmpty()) {
                            val drugInteraction = data[0].drugInteraction
                            val alcoholWarning = data[0].alcoholWarning

                            // 서버에서 받은 데이터 중 drugInteraction와 alcoholWarning를 UI에 반영
                            binding.warningNameTv.text = drugInteraction
                            binding.warningTextBlack2.text = alcoholWarning
                        }
                    }
                } else {
                    // 응답이 실패했을 경우 처리
                    Log.e("BottomSheetFragment3", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // 네트워크 오류 등으로 요청이 실패한 경우 처리
                Log.e("BottomSheetFragment3", "Failure: ${e.message}")
            }
        }
    }
}

