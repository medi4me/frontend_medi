package com.example.mediforme.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.databinding.ItemRoutineDrugBinding
import com.example.mediforme.remote.api.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoutineDrugRVAdaptor(
    private var drugRoutineList: ArrayList<RoutineDrug>,
    private val accessToken: String,
    private val apiService: ApiService,
    private val coroutineScope: CoroutineScope
) : RecyclerView.Adapter<RoutineDrugRVAdaptor.Holder>() {


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
            // 각 항목의 상태를 명확하게 업데이트
            drugTime.text = routineDrug.drugTime
            drugName.text = routineDrug.drugName
            drugNum.text = routineDrug.drugNum

            // 리스너를 먼저 제거한 후에 상태 설정
            drugCheckBtn.setOnCheckedChangeListener(null)
            drugCheckBtn.isChecked = routineDrug.drugCheckBtn

            // 체크 상태에 따라 선택바 보이기/숨기기
            if (routineDrug.drugCheckBtn) {
                selectBar.visibility = View.VISIBLE
                unselectBar.visibility = View.GONE
            } else {
                selectBar.visibility = View.GONE
                unselectBar.visibility = View.VISIBLE
            }

            // 체크 상태가 변경되면 서버에 업데이트 요청
            drugCheckBtn.setOnCheckedChangeListener { _, isChecked ->
                routineDrug.drugCheckBtn = isChecked
                notifyItemChanged(adapterPosition) // 클릭된 항목만 상태 업데이트

                if (isChecked) {
                    // 체크 상태 업데이트 요청
                    checkMedicineStatus(routineDrug.userMedicineId)
                } else {
                    // 체크 해제 상태 업데이트 요청
                    uncheckMedicineStatus(routineDrug.userMedicineId)
                }

                // 선택바 상태 업데이트
                if (isChecked) {
                    selectBar.visibility = View.VISIBLE
                    unselectBar.visibility = View.GONE
                } else {
                    selectBar.visibility = View.GONE
                    unselectBar.visibility = View.VISIBLE
                }
            }
        }

        private fun checkMedicineStatus(userMedicineId: Int) {
            coroutineScope.launch {
                try {
                    val response: Response<Void> = apiService.checkMedicine(accessToken, userMedicineId)
                    if (response.isSuccessful) {
                        // 성공적으로 업데이트됨
                        Log.d("RoutineDrugRVAdaptor", "Medicine checked successfully. ID: $userMedicineId")
                    } else {
                        // 업데이트 실패 처리
                        Log.e("RoutineDrugRVAdaptor", "Failed to check medicine. ID: $userMedicineId, Response code: ${response.code()}")
                    }
                } catch (e: Exception) {
                    // 네트워크 오류 처리
                    Log.e("RoutineDrugRVAdaptor", "Error checking medicine. ID: $userMedicineId", e)
                }
            }
        }

        private fun uncheckMedicineStatus(userMedicineId: Int) {
            coroutineScope.launch {
                try {
                    val response: Response<Void> = apiService.uncheckMedicine(accessToken, userMedicineId)
                    if (response.isSuccessful) {
                        // 성공적으로 업데이트됨
                        Log.d("RoutineDrugRVAdaptor", "Medicine unchecked successfully. ID: $userMedicineId")
                    } else {
                        // 업데이트 실패 처리
                        Log.e("RoutineDrugRVAdaptor", "Failed to uncheck medicine. ID: $userMedicineId, Response code: ${response.code()}")
                    }
                } catch (e: Exception) {
                    // 네트워크 오류 처리
                    Log.e("RoutineDrugRVAdaptor", "Error unchecking medicine. ID: $userMedicineId", e)
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
