package com.example.mediforme.search

import com.example.mediforme.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicineAdapter(private val medicines: List<Medicine>) : RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val medicineImage: ImageView = itemView.findViewById(R.id.medicine_image)
        private val medicineName: TextView = itemView.findViewById(R.id.medicine_name)
        private val medicineDosage: TextView = itemView.findViewById(R.id.medicine_dosage)

        fun bind(medicine: Medicine) {
            // medicineImage.setImageResource(...) // 이미지 설정, 필요시
            medicineName.text = medicine.name
            medicineDosage.text = medicine.dosage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medicine_bottomsheet, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        holder.bind(medicines[position])
    }

    override fun getItemCount(): Int {
        return medicines.size
    }
}

data class Medicine(val name: String, val dosage: String)