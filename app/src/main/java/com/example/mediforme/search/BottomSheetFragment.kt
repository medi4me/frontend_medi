package com.example.mediforme.search

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.parcel.Parcelize

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

    // 약 정보를 담은 리스트
    private val medicineInfoList = listOf(
        MedicineInfo("부타정", "아세트아미노펜과립", "0.7mg"),
        MedicineInfo("피프티정", "이부프로펜", "0.5mg"),
        MedicineInfo("타이레놀", "아세트아미노펜", "0.3mg")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet2, container, false)

        tabLayout = view.findViewById(R.id.tab_layout)
        recyclerView = view.findViewById(R.id.recycler_view)

        // RecyclerView 초기화 및 설정
        tabAdapter = TabAdapter(medicineInfoList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = tabAdapter

        // TabLayout과 RecyclerView를 연결합니다.
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val position = it.position
                    recyclerView.scrollToPosition(position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
        })

        // TabLayout의 탭을 설정합니다.
        for (i in medicineInfoList.indices) {
            tabLayout.addTab(tabLayout.newTab().setText(medicineInfoList[i].title))
        }

        return view
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet3, container, false)
    }
}


