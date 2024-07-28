package com.example.mediforme.home.todayCondition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.R
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

        // RecyclerView 설정
        val items = listOf(
            WeekDayItem("일", 1, R.drawable.ic_empty_emoji),
            WeekDayItem("월", 2, R.drawable.ic_emoji_good),
            WeekDayItem("화", 3, R.drawable.ic_empty_emoji),
            WeekDayItem("수", 4, R.drawable.ic_emoji_soso),
            WeekDayItem("목", 5, R.drawable.ic_emoji_good),
            WeekDayItem("금", 6, R.drawable.ic_empty_emoji),
            WeekDayItem("토", 7, R.drawable.ic_emoji_bad)
        )

        val adapter = WeekDayAdapter(items)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter


        return binding.root
    }
}