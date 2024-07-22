package com.example.mediforme.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    //바인딩 사용하려면 여기 처럼 해줘야 사용 가능 함 -- 1
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // DataBindingUtil을 사용하여 Binding 객체를 초기화합니다.
        //바인딩 사용하려면 여기 처럼 해줘야 사용 가능 함 -- 2
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //------------------ 배너 부분 ----------------------------//
        val bannerAdapter= BannerVPAdapter(this)
        //이미지도 함께 넣어줌 -> BannerFragment.kt에서 인자 값을 int형으로 받음
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        //뷰페이저와 어댑터 연결
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL //좌우로 스크롤 되게 함
        //배너와 인디케이터 연결
        binding.homeBannerIndicator.setViewPager((binding.homeBannerVp))
        return binding.root


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 데이터 생성
        val routineDrugList = ArrayList<RoutineDrug>()
        routineDrugList.add(RoutineDrug("오전 9시", "테스민정 0.1mg", "1정", true)) // 변경: Boolean 값 사용
        routineDrugList.add(RoutineDrug("오후 12시", "테스민 0.1mg", "2정", false)) // 변경: Boolean 값 사용
        routineDrugList.add(RoutineDrug("오후 12시", "테스민정 0.1mg", "2정", false)) // 변경: Boolean 값 사용
        routineDrugList.add(RoutineDrug("오후 12시", "테민정 0.1mg", "2정", false)) // 변경: Boolean 값 사용
        routineDrugList.add(RoutineDrug("오전 9시", "스민정 0.1mg", "1정", true)) // 변경: Boolean 값 사용
        routineDrugList.add(RoutineDrug("오후 12시", "테스민정 0.1mg", "2정", false)) // 변경: Boolean 값 사용

        // RecyclerView 설정 ==> 이 부분을 데이터 바인딩으로 사용할 수 도 있음,
        // 데이터 바인딩으로 하는 방법, xml에 <data></data> 여기 안에
        binding.homeRoutineRV.adapter = RoutineDrugRVAdaptor(routineDrugList)
        binding.homeRoutineRV.adapter = RoutineDrugRVAdaptor(routineDrugList)
        binding.homeRoutineRV.layoutManager = LinearLayoutManager(requireContext())




    }
}
