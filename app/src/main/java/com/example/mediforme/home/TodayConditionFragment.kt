package com.example.mediforme.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mediforme.R

class TodayConditionFragment : Fragment() {
    lateinit var binding: TodayConditionFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today_condition, container, false)
        //        binding = FragmentHomeBinding.inflate(inflater, container, false)
    //        바인딩으로 바꾸기 홈프래그먼트 보고
        //xml에 백버튼 달기
    }
}