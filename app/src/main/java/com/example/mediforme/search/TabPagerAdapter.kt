package com.example.mediforme.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(
    fragment: Fragment,
    private val tabTitles: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabTitles.size

    override fun createFragment(position: Int): Fragment {
        // 각 페이지에 표시할 프래그먼트를 반환합니다.
        return TabItemFragment.newInstance(tabTitles[position])
    }

    fun getTabTitle(position: Int): String {
        return tabTitles[position]
    }
}
