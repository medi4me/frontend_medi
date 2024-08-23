package com.example.mediforme.search

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mediforme.Data.MedicineInfoResponse
import com.example.mediforme.Data.MedicineService
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentBottomSheet3Binding
import com.example.mediforme.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.parcel.Parcelize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
class BottomSheetFragment2 : BottomSheetDialogFragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var tabAdapter: TabAdapter

    // 약 정보를 담을 리스트 (초기화는 나중에 서버 데이터로 대체)
    private var medicineInfoList: List<MedicineInfo> = emptyList()

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
        val retrofit = getRetrofit() // Retrofit 인스턴스를 가져오는 함수
        val service = retrofit.create(MedicineService::class.java)
        val call = service.getMedicineInfo(medicineNames)

        // medicineNames 리스트 로그로 출력
        Log.d("BottomSheetFragment2", "Requesting info for: $medicineNames")

        call.enqueue(object : Callback<List<MedicineInfoResponse>> {
            override fun onResponse(
                call: Call<List<MedicineInfoResponse>>,
                response: Response<List<MedicineInfoResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { responseList ->
                        // 서버로부터 받은 여러 개의 MedicineInfo를 리스트에 추가
                        medicineInfoList = responseList.map { response ->
                            MedicineInfo(
                                title = response.name,
                                ingredient = response.componentName,
                                amount = response.amount
                            )
                        }

                        // 서버에서 받아온 리스트 로그로 출력
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
                } else {
                    Log.e("BottomSheetFragment2", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<MedicineInfoResponse>>, t: Throwable) {
                Log.e("BottomSheetFragment2", "Request failed", t)
            }
        })
    }

    @Parcelize
    data class MedicineInfo(
        val title: String,
        val ingredient: String,
        val amount: String
    ) : Parcelable
}





// BottomSheetFragment3.kt
class BottomSheetFragment3 : BottomSheetDialogFragment() {
    lateinit var binding: FragmentBottomSheet3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheet3Binding.inflate(inflater, container, false)

        binding.veriBtnCombination.setOnClickListener {
            val intent = Intent(requireContext(), CheckMedicineActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}




