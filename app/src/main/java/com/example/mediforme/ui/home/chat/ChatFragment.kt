package com.example.mediforme.ui.home.chat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.databinding.FragmentChatBinding
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.request.QuestionRequestDto
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.ChatGptResponseDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private val authToken = "Bearer API키!!!!!!!!" // 여기에 실제 API 키를 입력하세요
    private lateinit var sharedPreferences: SharedPreferences
    private var userName:  String? = null // userName을 String 타입으로 선언

    // 의존성 주입을 통해 ApiService 인스턴스를 받음
    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        // SharedPreferences에서 저장된 사용자 이름 가져오기
        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        userName = sharedPreferences.getString("name", "Unknown Name")

        Log.d("dddkkk","${userName}")
        // 사용자 이름을 chat_name_TV에 설정
        binding.chatNameTV.text = userName

        // RecyclerView 설정
        chatAdapter = ChatAdapter(messages)
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }

        // 뒤로가기 버튼 클릭 시 이전 프래그먼트로 돌아감
        binding.howTodayBackBtnIV.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 전송 버튼 클릭 시 API 호출
        binding.sendIV.setOnClickListener {
            val userQuestion = binding.sendET.text.toString()
            if (userQuestion.isNotEmpty()) {
                sendMessage(userQuestion)
            } else {
                Toast.makeText(context, "질문을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun sendMessage(question: String) {
        // 사용자의 메시지를 추가하고 화면에 표시
        messages.add(Message(question, true))
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.scrollToPosition(messages.size - 1)

        // 메시지 전송 후 EditText의 내용을 지움
        binding.sendET.text.clear()

        // 코루틴으로 API 호출
        lifecycleScope.launch {
            try {
                val questionRequest = QuestionRequestDto(question)
                val response: Response<ApiResponse<ChatGptResponseDto>> = apiService.askQuestion(authToken, questionRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.isSuccess) {
                        val answer = apiResponse.result.choices.firstOrNull()?.message?.content ?: "응답을 받지 못했습니다."
                        receiveMessage(answer)
                    } else {
                        receiveMessage("API 응답 실패: ${apiResponse?.message ?: "알 수 없는 오류"}")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    receiveMessage("서버 응답 실패: ${response.message()} \n오류 내용: $errorBody")
                }
            } catch (e: Exception) {
                receiveMessage("오류 발생: ${e.message}")
            }
        }
    }

    private fun receiveMessage(message: String) {
        // 챗봇의 메시지를 추가하고 화면에 표시
        messages.add(Message(message, false))
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.scrollToPosition(messages.size - 1)
    }
}
