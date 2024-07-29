package com.example.mediforme.home.todayCondition

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentTodayConditionBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TodayConditionFragment : Fragment() {
    lateinit var binding: FragmentTodayConditionBinding
    private lateinit var adapter: WeekDayAdapter
    private lateinit var items: List<WeekDayItem>
    private var selectedDateItem: WeekDayItem? = null
    private var todayIndex: Int = 0


    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }


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
        // 날짜 데이터 초기화
        val weekData = getWeekDates()
        items = weekData.first
        todayIndex = weekData.second

        // RecyclerView 설정
        adapter = WeekDayAdapter(items) { dateItem ->
            selectedDateItem = dateItem
            onDateItemClick(dateItem)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter

        // 오늘 날짜 선택
        selectTodayDate()

        return binding.root
    }
    private fun selectTodayDate() {
        // 오늘 날짜 아이템을 선택 상태로 설정
        val todayItem = items[todayIndex]
        todayItem.isSelected = true
        selectedDateItem = todayItem
        adapter.notifyDataSetChanged()

        // 오늘 날짜로 스크롤
        binding.recyclerView.scrollToPosition(todayIndex)

        // 현재 날짜와 시간을 가져옴
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(Calendar.DAY_OF_MONTH, todayItem.date.toInt())
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val clickedDate = sdf.format(currentCalendar.time)
        binding.homeDate.text = clickedDate

        // 선택된 날짜에 해당하는 요일과 날짜로 업데이트
        binding.homeDate.text = getString(R.string.date_format, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, todayItem.date)
    }

    private fun onDateItemClick(dateItem: WeekDayItem) {
        // 날짜 아이템 클릭 시
        Log.d(TAG, "Clicked on date: ${dateItem.date}")

        // 모든 아이템의 isSelected 상태를 false로 설정
        items.forEach { it.isSelected = false }

        // 클릭한 아이템의 isSelected 상태를 true로 설정
        dateItem.isSelected = true

        // 어댑터에 알림을 보냄
        adapter.notifyDataSetChanged()

        // 현재 날짜와 시간을 가져옴
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(Calendar.MONTH, dateItem.month - 1) // Calendar.MONTH는 0부터 시작하므로 -1
        currentCalendar.set(Calendar.DAY_OF_MONTH, dateItem.date.toInt())
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val clickedDate = sdf.format(currentCalendar.time)
        binding.homeDate.text = clickedDate

        // 선택된 날짜에 해당하는 요일과 날짜로 업데이트
        binding.homeDate.text = getString(R.string.date_format, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, dateItem.date)
    }

}