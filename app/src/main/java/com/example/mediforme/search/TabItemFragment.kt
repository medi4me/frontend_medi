package com.example.mediforme.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mediforme.R

class TabItemFragment : Fragment() {

    companion object {
        private const val ARG_MEDICINE_INFO = "medicine_info"

        fun newInstance(medicineInfo: BottomSheetFragment2.MedicineInfo): TabItemFragment {
            val fragment = TabItemFragment()
            val args = Bundle().apply {
                putParcelable(ARG_MEDICINE_INFO, medicineInfo)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var medicineInfo: BottomSheetFragment2.MedicineInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicineInfo = it.getParcelable(ARG_MEDICINE_INFO) ?: throw IllegalArgumentException("MedicineInfo must not be null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab_item, container, false)

        val ingredientsTextView: TextView = view.findViewById(R.id.ingredients_contents_tv)
        val amountTextView: TextView = view.findViewById(R.id.amount_tv)

        ingredientsTextView.text = medicineInfo.ingredient
        amountTextView.text = medicineInfo.amount

        return view
    }
}
