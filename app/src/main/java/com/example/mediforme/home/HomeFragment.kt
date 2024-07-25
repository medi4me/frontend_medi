package com.example.mediforme.home

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
import androidx.navigation.fragment.findNavController
import com.example.mediforme.home.HomeFragmentDirections
import java.util.Timer

class HomeFragment : Fragment() {
    // 바인딩 사용하려면 여기 처럼 해줘야 사용 가능 함
    private lateinit var binding: FragmentHomeBinding
    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // DataBindingUtil을 사용하여 Binding 객체를 초기화합니다.
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 배너 부분 설정
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.homeBannerIndicator.setViewPager(binding.homeBannerVp)
        startAutoSlide(bannerAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // howTodayCV 클릭 리스너 설정
        binding.howTodayCV.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTodayConditionFragment3()
            findNavController().navigate(action)
        }

        // 더미 데이터 생성
        val routineDrugList = ArrayList<RoutineDrug>()
        routineDrugList.add(RoutineDrug("오전 9시", "테스민정 0.1mg", "1정", true))
        routineDrugList.add(RoutineDrug("오후 12시", "테스민 0.1mg", "2정", false))
        routineDrugList.add(RoutineDrug("오후 12시", "테스민정 0.1mg", "2정", false))
        routineDrugList.add(RoutineDrug("오후 12시", "테민정 0.1mg", "2정", false))
        routineDrugList.add(RoutineDrug("오전 9시", "스민정 0.1mg", "1정", true))
        routineDrugList.add(RoutineDrug("오후 12시", "테스민정 0.1mg", "2정", false))

        // RecyclerView 설정
        binding.homeRoutineRV.adapter = RoutineDrugRVAdaptor(routineDrugList)
        binding.homeRoutineRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun startAutoSlide(adapter: BannerVPAdapter) {
        // 일정 간격으로 슬라이드 변경 (3초마다)
        timer.scheduleAtFixedRate(3000, 3000) {
            handler.post {
                val nextItem = binding.homeBannerVp.currentItem + 1
                if (nextItem < adapter.itemCount) {
                    binding.homeBannerVp.currentItem = nextItem
                } else {
                    binding.homeBannerVp.currentItem = 0 // 마지막 페이지에서 첫 페이지로 순환
                }
            }
        }
    }
}
