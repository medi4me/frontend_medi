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
import java.util.Timer

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup ViewPager with BannerFragment
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment.newInstance(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.homeBannerIndicator.setViewPager(binding.homeBannerVp)
        startAutoSlide(bannerAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Click listener for "howTodayCV"
        binding.howTodayCV.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTodayConditionFragment3()
            findNavController().navigate(action)
        }

        // Dummy data for RecyclerView
        val routineDrugList = arrayListOf(
            RoutineDrug("오전 9시", "테스민정 0.1mg", "1정", true),
            RoutineDrug("오후 12시", "테스민 0.1mg", "2정", false),
            RoutineDrug("오후 12시", "테스민정 0.1mg", "2정", false),
            RoutineDrug("오후 12시", "테민정 0.1mg", "2정", false),
            RoutineDrug("오전 9시", "스민정 0.1mg", "1정", true),
            RoutineDrug("오후 12시", "테스민정 0.1mg", "2정", false)
        )

        // Setup RecyclerView
        binding.homeRoutineRV.adapter = RoutineDrugRVAdaptor(routineDrugList)
        binding.homeRoutineRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun startAutoSlide(adapter: BannerVPAdapter) {
        // Auto slide banners every 3 seconds
        timer.scheduleAtFixedRate(3000, 3000) {
            handler.post {
                val nextItem = binding.homeBannerVp.currentItem + 1
                if (nextItem < adapter.itemCount) {
                    binding.homeBannerVp.currentItem = nextItem
                } else {
                    binding.homeBannerVp.currentItem = 0 // Loop to first item
                }
            }
        }
    }
}
