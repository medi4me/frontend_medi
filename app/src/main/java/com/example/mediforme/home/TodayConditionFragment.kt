package com.example.mediforme.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentHomeBinding
import com.example.mediforme.databinding.FragmentTodayConditionBinding

class TodayConditionFragment : Fragment() {
    lateinit var binding: FragmentTodayConditionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodayConditionBinding.inflate(inflater,container,false)

        binding.howTodayBackBtnIV.setOnClickListener {
            // 뒤로가기 버튼 클릭 시 이전 프래그먼트로 돌아감
            parentFragmentManager.popBackStack()
        }
        return binding.root
    }
}