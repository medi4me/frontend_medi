package com.example.mediforme.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.databinding.ItemDrugContentBinding

class ContentDrugRVAdaptor(val contentDrugList: ArrayList<ContentDrug>): RecyclerView.Adapter<ContentDrugRVAdaptor.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDrugContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val contentDrug  = contentDrugList[position]
        holder.contentDrugImg.setImageResource(contentDrug.contentDrugImg)
        holder.contnetDrugName.text = contentDrug.contentDrugName
        holder.contnetDrugTime.text = contentDrug.contentDrugTime
        holder.contentDrugFrequency.text = contentDrug.contentDrugFrequency
        holder.contnetBell.setImageResource(contentDrug.contentDrugBell)

        // Set delete button click listener
        holder.itemDeleteCL.setOnClickListener {
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return contentDrugList.size
    }

    fun removeItem(position: Int) {
        contentDrugList.removeAt(position)
        notifyItemRemoved(position)
    }


    inner class Holder(val binding: ItemDrugContentBinding): RecyclerView.ViewHolder(binding.root) {
        val contentDrugImg = binding.myItemDrugImg
        val contnetDrugName = binding.myItemDrugName
        val contnetDrugTime = binding.myItemDrugTime
        val contentDrugFrequency = binding.myItemDrugFrequency
        val contnetBell = binding.myItemBellSwitch
        val itemDeleteCL = binding.itemDeleteCL
    }
}
