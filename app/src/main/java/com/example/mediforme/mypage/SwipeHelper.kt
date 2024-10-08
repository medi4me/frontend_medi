package com.example.mediforme.mypage

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.Data.MedicineDeleteService
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.R
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max
import kotlin.math.min

class SwipeHelper(
    private val context: Context, // Context 추가
    private val adapter: ContentDrugRVAdaptor
) : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

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
        adapter.removeItem(position)
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

            itemView.translationX = x
            eraseView.translationX = 0f
            eraseView.visibility = if (x < 0) View.VISIBLE else View.INVISIBLE

            eraseView.setOnClickListener {
                Log.d("delete", "삭제 클릭")
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    showDeleteAccountDialog(position)
                    //adapter.removeItem(position)
                }
            }

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

    //회원탈퇴 버튼 클릭 시 다이얼로그 표시 메소드
    private fun showDeleteAccountDialog(position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_drug, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // 외부 배경을 투명하게 설정,둥글게 보이기 위해서

        val backBtn = dialogView.findViewById<ImageView>(R.id.dialog_log_delete_xBtn_IV)
        val cancelBtn = dialogView.findViewById<Button>(R.id.dialog_log_delete_back_BTN)
        val deleteBtn = dialogView.findViewById<Button>(R.id.dialog_log_delete_BTN)

        backBtn.setOnClickListener{
            alertDialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        deleteBtn.setOnClickListener {
            // 약물 삭제 서버 요청
            val contentDrug = adapter.contentDrugList[position]
            val memberId = 1 // 실제 memberId를 사용해야 합니다.
            val userMedicineId = contentDrug.userMedicineId // 해당 약물의 userMedicineId를 사용해야 합니다.

            val retrofit = getRetrofit()
            val service = retrofit.create(MedicineDeleteService::class.java)

            // SharedPreferences에서 토큰 가져오기
            val accessToken = sharedPreferences.getString("accessToken", null)
            if (accessToken.isNullOrEmpty()) {
                Log.e("SwipeHelper", "Access token is missing. Cannot delete medicine.")
                return@setOnClickListener
            }

            val call = service.deleteMedicine("Bearer $accessToken", userMedicineId)

            Log.d("SwipeHelper", "Attempting to delete userMedicineId: $userMedicineId, memberId: $memberId")
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string() // 응답을 직접 읽음
                        Log.d("SwipeHelper", "Response: $responseBody")
                        // 성공적으로 삭제된 경우 로컬에서도 삭제 처리
                        adapter.removeItem(position)
                        alertDialog.dismiss()
                    } else {
                        Log.e("SwipeHelper", "Failed to delete medicine: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("SwipeHelper", "Error deleting medicine", t)
                }
            })
        }
        alertDialog.show()
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
