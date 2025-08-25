package com.example.mediforme.ui.home

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentHomeBinding
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.MedicineResponse
import com.example.mediforme.remote.model.response.Medicines
import com.example.mediforme.ui.home.chat.ChatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var weekDayAdapter: WeekDayAdapter2  // 요일에 사용하는 어댑터
    private lateinit var routineDrugAdapter: RoutineDrugRVAdaptor // 약물 리스트에 사용하는 어댑터
    private lateinit var items2: List<WeekDayItem2>
    private var selectedDateItem2: WeekDayItem2? = null
    private var todayIndex2: Int = 0

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var homeNameTV: TextView

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup ViewPager with BannerFragment
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp2))
//        binding.homeBannerVp.adapter = bannerAdapter
//        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        binding.homeBannerIndicator.setViewPager(binding.homeBannerVp)
//        startAutoSlide(bannerAdapter)

        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        homeNameTV = binding.homeNameTV

        val name = sharedPreferences.getString("name", "Unknown Name")

        // Set the retrieved values to the TextView
        homeNameTV.text = "$name"

        // 날짜 데이터 초기화
        val weekData2 = com.example.mediforme.ui.home.getWeekDates()
        items2 = weekData2.first
        todayIndex2 = weekData2.second

        // 요일 RecyclerView 설정
        weekDayAdapter = WeekDayAdapter2(items2) { dateItem ->
            selectedDateItem2 = dateItem
            onDateItemClick(dateItem)
        }
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView2.adapter = weekDayAdapter

        // 오늘 날짜 선택
        selectTodayDate()

        return binding.root
    }

    private fun selectTodayDate() {
        // 오늘 날짜 아이템을 선택 상태로 설정
        val todayItem = items2[todayIndex2]
        todayItem.isSelected = true
        selectedDateItem2 = todayItem
        weekDayAdapter.notifyDataSetChanged()

        // 오늘 날짜로 스크롤
        binding.recyclerView2.scrollToPosition(todayIndex2)

        // 현재 날짜와 시간을 가져옴
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(Calendar.DAY_OF_MONTH, todayItem.date.toInt())
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val clickedDate = sdf.format(currentCalendar.time)
        binding.homeDate.text = clickedDate

        // 선택된 날짜에 해당하는 요일과 날짜로 업데이트
        binding.homeDate.text = getString(R.string.date_format, currentCalendar.get(Calendar.YEAR), currentCalendar.get(
            Calendar.MONTH) + 1, todayItem.date)
    }

    private fun onDateItemClick(dateItem: WeekDayItem2) {
        // 날짜 아이템 클릭 시
        Log.d(TAG, "Clicked on date: ${dateItem.date}")

        // 모든 아이템의 isSelected 상태를 false로 설정
        items2.forEach { it.isSelected = false }

        // 클릭한 아이템의 isSelected 상태를 true로 설정
        dateItem.isSelected = true

        // 어댑터에 알림을 보냄
        weekDayAdapter.notifyDataSetChanged()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 오늘 어때요 클릭 시
        binding.howTodayCV.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTodayConditionFragment3()
            findNavController().navigate(action)
        }
        // 물어보기 클릭 시
        binding.askMeCV.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            startActivity(intent)
        }

        // SharedPreferences에서 저장된 토큰과 이름 가져오기
        val accessToken = sharedPreferences.getString("accessToken", "") ?: ""

        // 약물 리스트 RecyclerView 설정
        routineDrugAdapter = RoutineDrugRVAdaptor(arrayListOf(), accessToken, apiService, lifecycleScope)
        binding.homeRoutineRV.adapter = routineDrugAdapter
        binding.homeRoutineRV.layoutManager = LinearLayoutManager(requireContext())

        fetchMedicines(accessToken!!)
    }

    private fun fetchMedicines(token: String) {

        lifecycleScope.launch {
            try {
                val response: retrofit2.Response<ApiResponse<MedicineResponse>> = apiService.getUserMedicines(token)

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.isSuccess) {
                        val medicineList = apiResponse.result?.medicines ?: emptyList()
                        updateRecyclerView(medicineList)
                    } else {
                        Log.e(TAG, "API 응답 실패: ${apiResponse?.message}")
                        Toast.makeText(context, "API 응답 실패: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(TAG, "서버 응답 실패: ${response.code()}")
                    Toast.makeText(context, "서버 응답 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "API 호출 중 오류 발생", e)
                Toast.makeText(context, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRecyclerView(medicineList: List<Medicines>) {
        val routineDrugList = medicineList.map { medicine ->
            RoutineDrug(
                userMedicineId = medicine.userMedicineId,
                drugTime = medicine.time ?: "",
                drugName = medicine.itemName ?: "",
                drugNum = medicine.dosage ?: "",
                drugCheckBtn = medicine.check
            )
        }
        routineDrugAdapter.updateData(routineDrugList)
    }
//
//    private fun startAutoSlide(adapter: BannerVPAdapter) {
//        // Auto slide banners every 5 seconds
//        timer.scheduleAtFixedRate(5000, 5000) {
//            handler.post {
//                val nextItem = binding.homeBannerVp.currentItem + 1
//                if (nextItem < adapter.itemCount) {
//                    binding.homeBannerVp.currentItem = nextItem
//                } else {
//                    binding.homeBannerVp.currentItem = 0 // Loop to first item
//                }
//            }
//        }
//    }
}
