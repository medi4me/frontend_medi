package com.example.mediforme.mypage

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.Data.MedicineResponse
import com.example.mediforme.Data.MedicineShowService
import com.example.mediforme.Data.Medicines
import com.example.mediforme.Data.AuthService
import com.example.mediforme.Data.LogoutResponse
import com.example.mediforme.Data.ResignResponse
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.MainActivity
import com.example.mediforme.R
import com.example.mediforme.databinding.FragmentMypageBinding
import com.example.mediforme.login.LoginActivity
import com.example.mediforme.onboarding.OnboardingMedicineActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    lateinit var adapter: ContentDrugRVAdaptor

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myPageNameTV: TextView
    private lateinit var authService: AuthService

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

        authService = getRetrofit().create(AuthService::class.java)


        // 더미 데이터 생성
        val contentDrugList = arrayListOf(
            ContentDrug(1, R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", true),
            ContentDrug(1, R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", true),
            ContentDrug(1, R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일",true),
            ContentDrug(1, R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", false),
            ContentDrug(1, R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일",false),
            ContentDrug(1, R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", true),
            ContentDrug(1, R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일", true),
            ContentDrug(1, R.drawable.ic_drug_default, "타이레놀 500mg", "06:00 PM", "매일",true),
            ContentDrug(1, R.drawable.ic_drug_default, "테스트민 정 0.1mg", "09:00 AM", "매일", false),
            ContentDrug(1, R.drawable.ic_drug_default, "아스피린 100mg", "12:00 PM", "매일",false),
        )

        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        myPageNameTV = binding.myNameTV

        val memberID = sharedPreferences.getString("memberID", "Unknown ID")
        val name = sharedPreferences.getString("name", "Unknown Name")

        myPageNameTV.text = "$name"


        adapter = ContentDrugRVAdaptor(contentDrugList)
        binding.myDrugRV.adapter = adapter
        binding.myDrugRV.layoutManager = LinearLayoutManager(requireContext())
        binding.myDrugRV.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))


        val swipeHelper = SwipeHelper(requireContext(),adapter)
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.myDrugRV)

        // 서버에서 데이터를 가져와서 RecyclerView에 표시
        fetchMedicines()

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

    private fun fetchMedicines() {
        val retrofit = getRetrofit()
        val service = retrofit.create(MedicineShowService::class.java)
        val call = service.getUserMedicines(memberId = "1") // 사용자의 memberId로 변경해야 함

        call.enqueue(object : Callback<MedicineResponse> {
            override fun onResponse(call: Call<MedicineResponse>, response: Response<MedicineResponse>) {
                if (response.isSuccessful) {
                    val medicineList = response.body()?.medicines ?: emptyList()
                    updateRecyclerView(medicineList)
                } else {
                    // 서버에서 오류가 발생했을 때 처리
                }
            }

            override fun onFailure(call: Call<MedicineResponse>, t: Throwable) {
                // 네트워크 오류 또는 서버 오류 처리
            }
        })
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
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken != null) {
            val authToken = "Bearer $accessToken"

            authService.resign(authToken).enqueue(object : Callback<ResignResponse> {
                override fun onResponse(
                    call: Call<ResignResponse>,
                    response: Response<ResignResponse>
                ) {
                    if (response.isSuccessful) {
                        val resignResponse = response.body()
                        resignResponse?.let {
                            if (it.isSuccess) {
                                Toast.makeText(requireContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                clearSharedPreferences()
                                val intent = Intent(requireActivity(), LoginActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            } else {
                                Toast.makeText(requireContext(), "회원 탈퇴 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                        } ?: run {
                            Toast.makeText(requireContext(), "회원 탈퇴 실패: 응답이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "서버 오류로 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("MyPageFragment", "Resign failed with code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResignResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류로 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("MyPageFragment", "Resign failed: ${t.message}")
                }
            })
        } else {
            Toast.makeText(requireContext(), "액세스 토큰이 없습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
    private fun logout() {
        // SharedPreferences에서 액세스 토큰 불러오기
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken != null) {
            // Authorization 헤더에 Bearer 토큰 추가
            val authToken = "Bearer $accessToken"

            // 로그아웃 API 호출
            authService.logout(authToken).enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
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
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "네트워크 오류로 로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "액세스 토큰이 없습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }


    // SharedPreferences 초기화
    private fun clearSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.clear() // 모든 데이터 삭제
        editor.apply()
    }
}