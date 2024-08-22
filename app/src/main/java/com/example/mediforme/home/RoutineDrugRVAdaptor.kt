package com.example.mediforme.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.databinding.ItemRoutineDrugBinding

class RoutineDrugRVAdaptor(private var drugRoutineList: ArrayList<RoutineDrug>) : RecyclerView.Adapter<RoutineDrugRVAdaptor.Holder>() {

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

            drugCheckBtn.setOnCheckedChangeListener(null)

            drugCheckBtn.isChecked = routineDrug.drugCheckBtn

            if (routineDrug.drugCheckBtn) {
                selectBar.visibility = View.VISIBLE
                unselectBar.visibility = View.GONE
            } else {
                selectBar.visibility = View.GONE
                unselectBar.visibility = View.VISIBLE
            }

            drugCheckBtn.setOnCheckedChangeListener { _, isChecked ->
                routineDrug.drugCheckBtn = isChecked
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

    fun updateData(newDrugRoutineList: List<RoutineDrug>) {
        drugRoutineList.clear()
        drugRoutineList.addAll(newDrugRoutineList)
        notifyDataSetChanged()
    }
}
