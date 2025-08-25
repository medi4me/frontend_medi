package com.example.mediforme.ui.home.chat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediforme.databinding.FragmentChatBinding
import com.example.mediforme.remote.api.ApiService
import com.example.mediforme.remote.model.request.QuestionRequestDto
import com.example.mediforme.remote.model.response.ApiResponse
import com.example.mediforme.remote.model.response.ChatGptResponseDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

// Hilt를 사용하여 의존성 주입을 활성화
@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private lateinit var sharedPreferences: SharedPreferences
    private var userName:  String? = null // userName을 String 타입으로 선언

    // Hilt가 ApiService 인스턴스를 자동으로 주입
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // 코루틴과 의존성 주입방식을 사용 -> getRetrofit()을 호출하는 초기화 과정 생략
//        val retrofit = getRetrofit()
//        apiService = retrofit.create(ApiService::class.java)

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

        // 코루틴으로 API 호출
        // lifecycleScope를 사용해 Activity의 생명주기에 맞춰 코루틴을 관리
        lifecycleScope.launch {
            try {
                val questionRequest = QuestionRequestDto(question)
                // suspend 함수를 호출하여 비동기 작업을 처리합니다.
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
