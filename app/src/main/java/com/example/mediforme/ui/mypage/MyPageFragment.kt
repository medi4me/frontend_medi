package com.example.mediforme.ui.mypage

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentMypageBinding
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.LogoutResponse
import com.example.mediforme.remote.model.response.MedicineResponse
import com.example.mediforme.remote.model.response.Medicines
import com.example.mediforme.remote.model.response.ResignResponse
import com.example.mediforme.ui.login.LoginActivity
import com.example.mediforme.ui.onboarding.OnboardingMedicineActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    lateinit var adapter: ContentDrugRVAdaptor

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myPageNameTV: TextView
    private var accessToken: String? = null

    // Hilt를 통해 ApiService 인스턴스 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        accessToken = sharedPreferences.getString("accessToken", "") ?: ""


//        // 더미 데이터 생성
//        val contentDrugList = arrayListOf(
//            ContentDrug(1, R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", true),
//            ContentDrug(1, R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", true),
//            ContentDrug(1, R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일",true),
//            ContentDrug(1, R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", false),
//            ContentDrug(1, R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일",false),
//            ContentDrug(1, R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", true),
//            ContentDrug(1, R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", true),
//            ContentDrug(1, R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일",true),
//            ContentDrug(1, R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", false),
//            ContentDrug(1, R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일",false),
//        )
//
//        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        myPageNameTV = binding.myNameTV

        val memberID = sharedPreferences.getString("memberID", "Unknown ID")
        val name = sharedPreferences.getString("name", "Unknown Name")

        myPageNameTV.text = "$name"

        val token = "Bearer ${accessToken}"
        adapter = ContentDrugRVAdaptor(arrayListOf(), apiService, viewLifecycleOwner.lifecycleScope, token)
        binding.myDrugRV.adapter = adapter
        binding.myDrugRV.layoutManager = LinearLayoutManager(requireContext())
        binding.myDrugRV.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))


        val swipeHelper = SwipeHelper(requireContext(), adapter, apiService, viewLifecycleOwner.lifecycleScope, token)
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.myDrugRV)

        // 서버에서 데이터를 가져와서 RecyclerView에 표시
        fetchMedicines(token)

        // 추가하기 버튼 클릭시 온보딩 화면으로 전환
        binding.myPlusBtnBtn.setOnClickListener {
            startActivity(Intent(requireContext(), OnboardingMedicineActivity::class.java))
        }

        // 회원탈퇴 버튼 클릭 시 다이얼로그 표시
        binding.myTextDeleteTV.setOnClickListener {
            showDeleteAccountDialog()
        }
        // 로그아웃 버튼 클릭 시 다이얼로그 표시
        binding.myTextLogoutTV.setOnClickListener {
            this.showLogoutAccountDialog()
        }


    }

    private fun fetchMedicines(token: String) {
        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<MedicineResponse>> = apiService.getUserMedicines(token)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val medicineList = apiResponse?.result?.medicines ?: emptyList()
                    updateRecyclerView(medicineList)
                } else {
                    Log.e("MyPageFragment", "Failed to fetch medicines: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MyPageFragment", "Error fetching medicines", e)
            }
        }
    }

    private fun updateRecyclerView(medicineList: List<Medicines>) {
        val contentDrugList = medicineList.map { medicine ->
            ContentDrug(
                userMedicineId = medicine.userMedicineId,
                contentDrugImg = R.drawable.ic_drug_default, // 이미지가 정해져 있지 않다면 기본 이미지를 사용
                contentDrugName = medicine.itemName ?: "",
                contentDrugTime = "매일", // 항상 "매일"로 설정
                contentDrugFrequency = medicine.time ?: "",
                isBellOn = medicine.alarm
            )
        }
        adapter.updateData(contentDrugList)
    }

    //로그아웃 버튼 클릭 시 다이얼로그 표시 메소드
    private fun showLogoutAccountDialog() {
        val dialogView1 = LayoutInflater.from(context).inflate(R.layout.dialog_log_out, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView1)
            .setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // 외부 배경을 투명하게 설정,둥글게 보이기 위해서
       // val logoutBackBtn = dialogView1.findViewById<ImageView>(R.id.dialog_log_out_xBtn_IV)
        val loginBtn = dialogView1.findViewById<Button>(R.id.dialog_log_out_login_BTN)

        loginBtn.setOnClickListener{
            //로그인 액티비티 뜨게 변경예정 !
            logout()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    //회원탈퇴 버튼 클릭 시 다이얼로그 표시 메소드
    private fun showDeleteAccountDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_log_delete, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
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
            // 회원탈퇴 처리 로직 추가
            resign()
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
    // 회원탈퇴 처리 메서드
    private fun resign() {
        val token = "Bearer ${accessToken}"
        if (accessToken.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "액세스 토큰이 없습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            return
        }

        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<ResignResponse>> = apiService.resign(token)
                if (response.isSuccessful) {
                    val resignResponse = response.body()
                    resignResponse?.let {
                        if (it.isSuccess) {
                            // 회원탈퇴 성공
                            Toast.makeText(requireContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                            // SharedPreferences 초기화 (회원탈퇴 처리)
                            clearSharedPreferences()

                            // 로그인 화면으로 이동
                            val intent = Intent(requireActivity(), LoginActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            // 회원탈퇴 실패 메시지 처리
                            Toast.makeText(requireContext(), "회원 탈퇴 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "서버 오류로 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("MyPageFragment", "Resign failed with code: ${response.code()}")
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "네트워크 오류로 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show()
                Log.e("MyPageFragment", "Resign failed: ${e.message}")
            }
        }
    }
    private fun logout() {
        val token = "Bearer ${accessToken}"
        if (accessToken.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "액세스 토큰이 없습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            return
        }

        lifecycleScope.launch {
            try {
                val response: Response<ApiResponse<LogoutResponse>> = apiService.logout(token)
                if (response.isSuccessful) {
                    val logoutResponse = response.body()
                    logoutResponse?.let {
                        if (it.isSuccess) {
                            // 로그아웃 성공
                            Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

                            // SharedPreferences 초기화 (로그아웃 처리)
                            clearSharedPreferences()

                            // 로그인 화면으로 이동
                            val intent = Intent(requireActivity(), LoginActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            // 로그아웃 실패 메시지 처리
                            Toast.makeText(requireContext(), "로그아웃 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "서버 오류로 로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "네트워크 오류로 로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // SharedPreferences 초기화
    private fun clearSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear() // 모든 데이터 삭제
        editor.apply()
    }
}