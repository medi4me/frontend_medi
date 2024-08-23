package com.example.mediforme.home.chat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.Data.getRetrofit
import com.example.mediforme.databinding.FragmentChatBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private lateinit var apiService: ChatGptApiService
    private lateinit var sharedPreferences: SharedPreferences
    private var userName:  String? = null // userName을 String 타입으로 선언


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit 초기화 및 API 서비스 생성
        val retrofit: Retrofit = getRetrofit()
        apiService = retrofit.create(ChatGptApiService::class.java)

        // RecyclerView 설정
        chatAdapter = ChatAdapter(messages)
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }
        // SharedPreferences에서 저장된 사용자 이름 가져오기
        sharedPreferences = this.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        userName = sharedPreferences.getString("name", "Unknown Name")

        Log.d("dddkkk","${userName}")
        // 사용자 이름을 chat_name_TV에 설정
        binding.chatNameTV.text = userName


        // 뒤로가기 버튼 클릭 시 액티비티 종료
        binding.howTodayBackBtnIV.setOnClickListener {
            finish()
        }

        // 전송 버튼 클릭 시 API 호출
        binding.sendIV.setOnClickListener {
            val userQuestion = binding.sendET.text.toString()
            if (userQuestion.isNotEmpty()) {
                sendMessage(userQuestion)
            } else {
                Toast.makeText(this, "질문을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendMessage(question: String) {
        // 사용자의 메시지를 추가하고 화면에 표시
        messages.add(Message(question, true))
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.scrollToPosition(messages.size - 1)

        // 메시지 전송 후 EditText의 내용을 지움
        binding.sendET.text.clear()

        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val authToken = "Bearer ${sharedPreferences.getString("accessToken", "")}"

        Log.d("token", "토큰: $authToken")

        if (authToken.isNullOrEmpty() || authToken == "Bearer ") {
            receiveMessage("토큰이 없습니다. 로그인하세요.")
            return
        }

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
