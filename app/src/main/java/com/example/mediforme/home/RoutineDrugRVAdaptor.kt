package com.example.mediforme.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.databinding.ItemRoutineDrugBinding
class RoutineDrugRVAdaptor(private val drugRoutineList: ArrayList<RoutineDrug>) : RecyclerView.Adapter<RoutineDrugRVAdaptor.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRoutineDrugBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val routineDrug = drugRoutineList[position]
        holder.bind(routineDrug)
    }

    override fun getItemCount(): Int {
        return drugRoutineList.size
    }

    inner class Holder(private val binding: ItemRoutineDrugBinding) : RecyclerView.ViewHolder(binding.root) {
        private val drugTime = binding.homeRoutineTimeTV
        private val drugName = binding.homeDrugNameTV
        private val drugNum = binding.homeDrugNumTV
        private val drugCheckBtn = binding.homeRoutineCheckBtnIV
        private val selectBar = binding.homeItemSelectBar
        private val unselectBar = binding.homeItemUnselectBar

        fun bind(routineDrug: RoutineDrug) {
            drugTime.text = routineDrug.drugTime
            drugName.text = routineDrug.drugName
            drugNum.text = routineDrug.drugNum

            // CheckBox의 상태 설정
            drugCheckBtn.isChecked = routineDrug.drugCheckBtn

            // 리스너를 설정하기 전에 이전 리스너를 제거
            drugCheckBtn.setOnCheckedChangeListener(null)

            // CheckBox 상태에 따른 선택바 보이기/숨기기
            if (routineDrug.drugCheckBtn) {
                selectBar.visibility = View.VISIBLE
                unselectBar.visibility = View.GONE
            } else {
                selectBar.visibility = View.GONE
                unselectBar.visibility = View.VISIBLE
            }

            // CheckBox의 상태 변경 리스너 설정
            drugCheckBtn.setOnCheckedChangeListener { _, isChecked ->
                routineDrug.drugCheckBtn = isChecked
                // CheckBox 상태에 따른 선택바 보이기/숨기기 업데이트
                if (isChecked) {
                    selectBar.visibility = View.VISIBLE
                    unselectBar.visibility = View.GONE
                } else {
                    selectBar.visibility = View.GONE
                    unselectBar.visibility = View.VISIBLE
                }
            }
        }
    }
}
