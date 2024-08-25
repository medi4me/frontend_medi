package com.example.mediforme.home.chat

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// 챗봇 interface
interface ChatGptApiService {
    @POST("/chat-gpt/question")
    fun askQuestion(
        @Header("Authorization") authToken: String,
        @Body questionRequest: QuestionRequestDto
    ): Call<ChatGptResponseDto>
}


data class QuestionRequestDto(
    val question: String
)

data class ChatGptResponseDto(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>
)

data class Choice(
    val message: MessageContent,
    val index: Int,
    val finish_reason: String
)

data class MessageContent(
    val role: String,
    val content: String
)