package com.example.mediforme.mypage

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.R
import kotlin.math.max
import kotlin.math.min


class SwipeHelper : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val view = getView(viewHolder)
        clamp = view.width.toFloat() / 10 * 2

        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        // 실제 아이템 삭제 로직
        (viewHolder.itemView.context as? MyPageFragment)?.adapter?.removeItem(position)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        getDefaultUIUtil().clearView(getView(viewHolder))
        previousPosition = viewHolder.adapterPosition
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val eraseView = view.findViewById<View>(R.id.erase_item_view)
            val itemView = view.findViewById<LinearLayout>(R.id.item_view)
            val isClamped = getClamped(viewHolder as ContentDrugRVAdaptor.Holder)

            val x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)
            currentDx = x

            // 상단 아이템 뷰(item_view)만 스와이프 이동
            itemView.translationX = x

            // 하단 삭제 view(erase_item_view)는 움직이지 않고 고정
            eraseView.translationX = 0f

            // 스와이프 시 erase_item_view 보이도록 설정
            eraseView.visibility = if (x < 0) View.VISIBLE else View.INVISIBLE

            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ): Float {
        val maxSwipe: Float = -view.width.toFloat() / 10 * 3
        val right: Float = 0f

        val x = if (isClamped) {
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            dX
        }

        return min(max(maxSwipe, x), right)
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        setClamped(viewHolder as ContentDrugRVAdaptor.Holder, currentDx <= -clamp)
        return 2f
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        return (viewHolder as ContentDrugRVAdaptor.Holder).itemView
    }

    private fun setClamped(viewHolder: ContentDrugRVAdaptor.Holder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    private fun getClamped(viewHolder: ContentDrugRVAdaptor.Holder): Boolean {
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition) return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setClamped(viewHolder as ContentDrugRVAdaptor.Holder, false)
            previousPosition = null
        }
    }
}
