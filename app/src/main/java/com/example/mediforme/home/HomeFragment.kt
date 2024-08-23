package com.example.mediforme.home

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentHomeBinding
import kotlin.concurrent.scheduleAtFixedRate
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.mediforme.Data.MedicineResponse
import com.example.mediforme.Data.MedicineShowService
import com.example.mediforme.Data.Medicines
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.home.todayCondition.WeekDayAdapter
import com.example.mediforme.home.todayCondition.WeekDayItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
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
    private var accessToken: String? = null

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

        // SharedPreferences에서 저장된 토큰과 이름 가져오기
        accessToken = sharedPreferences.getString("accessToken", "") ?: ""
        val name = sharedPreferences.getString("name", "Unknown Name")

        // Set the retrieved values to the TextView
        homeNameTV.text = "$name"

        // 날짜 데이터 초기화
        val weekData2 = com.example.mediforme.home.getWeekDates()
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
        binding.askMeCV.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToChatFragment()
            findNavController().navigate(action)
        }

        // 약물 리스트 RecyclerView 설정
        routineDrugAdapter = RoutineDrugRVAdaptor(arrayListOf(), requireContext())
        binding.homeRoutineRV.adapter = routineDrugAdapter
        binding.homeRoutineRV.layoutManager = LinearLayoutManager(requireContext())

        fetchMedicines()
    }

    private fun fetchMedicines() {
        val retrofit = getRetrofit()
        val service = retrofit.create(MedicineShowService::class.java)
        val call = service.getUserMedicines("Bearer $accessToken") // 사용자의 memberId로 변경해야 함

        call.enqueue(object : Callback<MedicineResponse> {
            override fun onResponse(call: Call<MedicineResponse>, response: Response<MedicineResponse>) {
                if (response.isSuccessful) {
                    val medicineList = response.body()?.medicines ?: emptyList()
                    updateRecyclerView(medicineList)
                } else {
                    Log.e(TAG, "Failed to fetch medicines: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MedicineResponse>, t: Throwable) {
                Log.e(TAG, "Error fetching medicines", t)
            }
        })
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
