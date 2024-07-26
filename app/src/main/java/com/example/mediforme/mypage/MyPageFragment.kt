package com.example.mediforme.mypage

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    lateinit var adapter: ContentDrugRVAdaptor


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =FragmentMypageBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 데이터 생성
        val contentDrugList = arrayListOf(
            ContentDrug(R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", R.drawable.ic_bell_off),
            ContentDrug(R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", R.drawable.ic_bell_off),
            ContentDrug(R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", R.drawable.ic_bell_on),
            ContentDrug(R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", R.drawable.ic_bell_off),
            ContentDrug(R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일", R.drawable.ic_bell_on)


        )


        adapter = ContentDrugRVAdaptor(contentDrugList)
        binding.myDrugRV.adapter = adapter
        binding.myDrugRV.layoutManager = LinearLayoutManager(requireContext())
        binding.myDrugRV.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))


        // ItemTouchHelper 설정
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 스와이프 시 항목 삭제
                val position = viewHolder.adapterPosition
                adapter.removeItem(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                // 스와이프 시 삭제 배경 그리기
                val itemView = viewHolder.itemView
                val paint = Paint()
                paint.color = Color.RED
                val icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_delete) }

                if (dX < 0) { // 스와이프 왼쪽
                    // 배경 그리기
                    c.drawRect(
                        itemView.right.toFloat() + dX, itemView.top.toFloat(),
                        itemView.right.toFloat(), itemView.bottom.toFloat(), paint
                    )

                    // 아이콘 그리기
                    icon?.setBounds(
                        itemView.right - icon.intrinsicWidth - 20,
                        itemView.top + (itemView.height - icon.intrinsicHeight) / 2,
                        itemView.right - 20,
                        itemView.top + (itemView.height + icon.intrinsicHeight) / 2
                    )
                    icon?.draw(c)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.myDrugRV)
    }
}