package com.example.mediforme.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mediforme.R

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

class TabAdapter(
    private var medicineInfoList: List<BottomSheetFragment2.MedicineInfo>
) : RecyclerView.Adapter<TabAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientsTextView: TextView = view.findViewById(R.id.ingredients_contents_tv)
        val amountTextView: TextView = view.findViewById(R.id.amount_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tab_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicineInfo = medicineInfoList[position]
        holder.ingredientsTextView.text = medicineInfo.ingredient
        holder.amountTextView.text = medicineInfo.amount
    }

    override fun getItemCount(): Int = medicineInfoList.size

    fun updateData(newMedicineInfoList: List<BottomSheetFragment2.MedicineInfo>) {
        medicineInfoList = newMedicineInfoList
        notifyDataSetChanged()
    }
}

