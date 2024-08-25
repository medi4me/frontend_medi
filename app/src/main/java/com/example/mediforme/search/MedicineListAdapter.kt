package com.example.mediforme.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediforme.R

class MedicineListAdapter(private val medicines: List<MedicineList>) : RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder>() {

    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val medicineImage: ImageView = itemView.findViewById(R.id.item_album_img_iv)
        private val medicineName: TextView = itemView.findViewById(R.id.item_medicine_title_tv)
        private val medicineDosage: TextView = itemView.findViewById(R.id.item_medicine_dose_tv)
        private val effects: TextView = itemView.findViewById(R.id.effects_content_tv)
        private val howToEat: TextView = itemView.findViewById(R.id.how_to_eat_content_tv)

        fun bind(medicine: MedicineList) {
            // Glide를 사용하여 이미지 로드
            Glide.with(itemView.context)
                .load(medicine.imageResId) // URL로부터 이미지 로드
                .placeholder(R.drawable.ic_drug_default) // 로드 중에 보여줄 기본 이미지
                .error(R.drawable.ic_drug_default) // 에러가 발생했을 때 보여줄 이미지
                .into(medicineImage)

            medicineName.text = medicine.name
            medicineDosage.text = ""
            effects.text = medicine.effects
            howToEat.text = medicine.howToEat
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medicine, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.bind(medicines[position])
    }

    override fun getItemCount(): Int {
        return medicines.size
    }
}


data class MedicineList(
    val imageResId: String?, // 이미지 리소스 ID
    val name: String,
    val dosage: String,
    val effects: String = "", // BottomSheetDialog에서만 사용되는 필드
    val howToEat: String = "" // BottomSheetDialog에서만 사용되는 필드
)
