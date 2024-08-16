package com.example.mediforme.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R
import com.example.mediforme.databinding.ItemDrugContentBinding

class ContentDrugRVAdaptor(val contentDrugList: ArrayList<ContentDrug>) :
    RecyclerView.Adapter<ContentDrugRVAdaptor.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDrugContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val contentDrug = contentDrugList[position]
        holder.contentDrugImg.setImageResource(contentDrug.contentDrugImg)
        holder.contnetDrugName.text = contentDrug.contentDrugName
        holder.contnetDrugTime.text = contentDrug.contentDrugTime
        holder.contentDrugFrequency.text = contentDrug.contentDrugFrequency
        // boolean 값에 따라 아이콘 설정
        val bellIcon = if (contentDrug.isBellOn) R.drawable.ic_bell_on else R.drawable.ic_bell_off
        holder.contentDrugBell.setImageResource(bellIcon)

        // 알림 아이콘에 클릭 리스너 설정
        holder.contentDrugBell.setOnClickListener {
            contentDrug.isBellOn = !contentDrug.isBellOn // 상태 토글
            val newBellIcon = if (contentDrug.isBellOn) R.drawable.ic_bell_on else R.drawable.ic_bell_off
            holder.contentDrugBell.setImageResource(newBellIcon)
            val toastMessage = if (contentDrug.isBellOn) "알림 On" else "알림 Off"
            Toast.makeText(holder.itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }


    //리사이클러뷰 재사용 해결 코드, 아래 부분을 지웠다가 다시 하는 듯
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return contentDrugList.size
    }

    fun removeItem(position: Int) {
        contentDrugList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class Holder(val binding: ItemDrugContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val contentDrugImg = binding.myItemDrugImg
        val contnetDrugName = binding.myItemDrugName
        val contnetDrugTime = binding.myItemDrugTime
        val contentDrugFrequency = binding.myItemDrugFrequency
        val contentDrugBell = binding.myItemBellSwitch

    }
}
