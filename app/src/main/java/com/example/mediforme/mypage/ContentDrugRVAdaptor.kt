package com.example.mediforme.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.Data.MedicineAlarmOffService
import com.example.mediforme.Data.MedicineAlarmService
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.R
import com.example.mediforme.databinding.ItemDrugContentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContentDrugRVAdaptor(val contentDrugList: ArrayList<ContentDrug>) :
    RecyclerView.Adapter<ContentDrugRVAdaptor.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDrugContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val contentDrug = contentDrugList[position]
        holder.bind(contentDrug)
    }

    override fun getItemCount(): Int {
        return contentDrugList.size
    }

    inner class Holder(val binding: ItemDrugContentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val contentDrugImg = binding.myItemDrugImg
        private val contnetDrugName = binding.myItemDrugName
        private val contnetDrugTime = binding.myItemDrugTime
        private val contentDrugFrequency = binding.myItemDrugFrequency
        private val contentDrugBell = binding.myItemBellSwitch

        fun bind(contentDrug: ContentDrug) {
            contentDrugImg.setImageResource(contentDrug.contentDrugImg)
            contnetDrugName.text = contentDrug.contentDrugName
            contnetDrugTime.text = contentDrug.contentDrugTime
            contentDrugFrequency.text = contentDrug.contentDrugFrequency

            // 알림 상태를 설정
            val bellIcon = if (contentDrug.isBellOn) R.drawable.ic_bell_on else R.drawable.ic_bell_off
            contentDrugBell.setImageResource(bellIcon)

            // 클릭 리스너를 설정하기 전에 이전 리스너를 제거
            contentDrugBell.setOnClickListener(null)

            // 알림 버튼 클릭 리스너 설정
            contentDrugBell.setOnClickListener {
                contentDrug.isBellOn = !contentDrug.isBellOn // 상태 토글
                val newBellIcon = if (contentDrug.isBellOn) R.drawable.ic_bell_on else R.drawable.ic_bell_off
                contentDrugBell.setImageResource(newBellIcon)

                // 알림 상태에 따라 서버에 업데이트 요청
                if (contentDrug.isBellOn) {
                    checkMedicineAlarmStatus(contentDrug.userMedicineId)
                } else {
                    uncheckMedicineAlarmStatus(contentDrug.userMedicineId)
                }

                val toastMessage = if (contentDrug.isBellOn) "알림 On" else "알림 Off"
                Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }

        private fun checkMedicineAlarmStatus(userMedicineId: Int) {
            val retrofit = getRetrofit() // Retrofit 인스턴스 가져오기
            val service = retrofit.create(MedicineAlarmService::class.java)
            val call = service.checkMedicineAlarm(userMedicineId)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // 성공적으로 업데이트됨
                    } else {
                        // 업데이트 실패 처리
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // 네트워크 오류 처리
                }
            })
        }

        private fun uncheckMedicineAlarmStatus(userMedicineId: Int) {
            val retrofit = getRetrofit() // Retrofit 인스턴스 가져오기
            val service = retrofit.create(MedicineAlarmOffService::class.java)
            val call = service.uncheckMedicineAlarm(userMedicineId)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // 성공적으로 업데이트됨
                    } else {
                        // 업데이트 실패 처리
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // 네트워크 오류 처리
                }
            })
        }
    }

    fun updateData(newContentDrugList: List<ContentDrug>) {
        contentDrugList.clear()
        contentDrugList.addAll(newContentDrugList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        contentDrugList.removeAt(position)
        notifyItemRemoved(position)
    }
}
