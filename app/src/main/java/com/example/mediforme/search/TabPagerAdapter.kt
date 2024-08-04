package com.example.mediforme.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(
    fragment: Fragment,
    private val medicineInfoList: List<BottomSheetFragment2.MedicineInfo>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = medicineInfoList.size

    override fun createFragment(position: Int): Fragment {
        return TabItemFragment.newInstance(medicineInfoList[position])
    }

    fun getTabTitle(position: Int): String {
        return medicineInfoList[position].title
    }
}
