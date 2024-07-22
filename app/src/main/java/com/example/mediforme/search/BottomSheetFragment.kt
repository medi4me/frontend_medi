package com.example.mediforme.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        val dummyData = listOf(
            Medicine("부타정", "0.7mg"),
            Medicine("파프티정", "0.5mg"),
            Medicine("타이레놀", "0.3mg")
        )

        // 어댑터 설정
        val adapter = MedicineAdapter(dummyData)
        binding.medicineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.medicineRecyclerView.adapter = adapter

        binding.addMedicineButton.setOnClickListener {
            Toast.makeText(requireActivity(), "버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        return binding.root
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