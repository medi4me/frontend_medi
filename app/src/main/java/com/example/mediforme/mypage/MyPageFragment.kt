package com.example.mediforme.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =FragmentMypageBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 데이터 생성
        val contentDrugList = arrayListOf(
            ContentDrug(R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", R.drawable.ic_bell_off),
            ContentDrug(R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", R.drawable.ic_bell_off),
            ContentDrug(R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", R.drawable.ic_bell_off),
            ContentDrug(R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일", R.drawable.ic_bell_on)


        )

        binding.myDrugRV.adapter = ContentDrugRVAdaptor(contentDrugList)
        binding.myDrugRV.layoutManager = LinearLayoutManager(requireContext())
        binding.myDrugRV.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

    }
}