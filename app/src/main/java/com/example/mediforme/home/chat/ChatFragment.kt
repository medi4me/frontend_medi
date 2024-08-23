package com.example.mediforme.home.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.databinding.FragmentChatBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private lateinit var apiService: ChatGptApiService
    private val authToken = "Bearer API키!!!!!!!!" // 여기에 실제 API 키를 입력하세요

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        // Retrofit 초기화 및 API 서비스 생성
        val retrofit: Retrofit = getRetrofit()
        apiService = retrofit.create(ChatGptApiService::class.java)

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

        // API 호출
        val questionRequest = QuestionRequestDto(question)
        apiService.askQuestion(authToken, questionRequest).enqueue(object : Callback<ChatGptResponseDto> {
            override fun onResponse(call: Call<ChatGptResponseDto>, response: Response<ChatGptResponseDto>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val answer = it.choices.firstOrNull()?.message?.content ?: "응답을 받지 못했습니다."
                        receiveMessage(answer)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    receiveMessage("서버 응답 실패: ${response.message()} \n오류 내용: $errorBody")
                }
            }

            override fun onFailure(call: Call<ChatGptResponseDto>, t: Throwable) {
                receiveMessage("오류 발생: ${t.message}")
            }
        })
    }

    private fun receiveMessage(message: String) {
        // 챗봇의 메시지를 추가하고 화면에 표시
        messages.add(Message(message, false))
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.scrollToPosition(messages.size - 1)
    }
}
