package com.example.mediforme.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.databinding.ItemRoutineDrugBinding

class RoutineDrugRVAdaptor(val drougRoutineList: ArrayList<RoutineDrug>):RecyclerView.Adapter<RoutineDrugRVAdaptor.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRoutineDrugBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val routineDrug = drougRoutineList[position]
        holder.drugTime.text = routineDrug.drugTime
        holder.drugName.text = routineDrug.drugName
        holder.drugNum.text = routineDrug.drugNum

        // CheckBox의 상태를 설정
        holder.drugCheckBtn.isChecked = routineDrug.drugCheckBtn

        // CheckBox의 상태가 변경될 때 RoutineDrug의 속성도 업데이트
        holder.drugCheckBtn.setOnCheckedChangeListener { _, isChecked ->
            routineDrug.drugCheckBtn = isChecked
            // 필요 시, 체크 상태를 저장하는 로직을 추가할 수 있습니다.
        }
    }

    override fun getItemCount(): Int {
        return drougRoutineList.size
    }

    inner class Holder(val binding: ItemRoutineDrugBinding): RecyclerView.ViewHolder(binding.root){
        val drugTime = binding.homeRoutineTimeTV
        val drugName = binding.homeDrugNameTV
        val drugNum = binding.homeDrugNumTV
        val drugCheckBtn = binding.homeRoutineCheckBtnIV
    }
}