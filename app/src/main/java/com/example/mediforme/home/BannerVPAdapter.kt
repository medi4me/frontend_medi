package com.example.mediforme.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) :FragmentStateAdapter(fragment) {
    //꼭필요한 함수 오버라이드해서 사용
    //리스트에 여러개의 프래그먼트 보관
    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    //데이터를 몇개를 전달할지
    override fun getItemCount(): Int = fragmentlist.size

    override fun createFragment(position: Int): Fragment = fragmentlist[position] //0,1,2,3,4,

    fun addFragment(fragment:Fragment){
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1) // 리스트에 새로운 값을 추가한걸 알려준다.
    }

}