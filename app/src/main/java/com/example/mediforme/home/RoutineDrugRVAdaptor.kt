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
        holder.drugTime.text = drougRoutineList[position].drugTime
        holder.drugName.text = drougRoutineList[position].drugName
        holder.drugNum.text = drougRoutineList[position].drugNum
        holder.drugCheckBtn.setImageResource(drougRoutineList[position].drugCheckBtn)
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