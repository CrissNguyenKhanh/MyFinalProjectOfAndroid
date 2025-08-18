package com.example.retrofitwrooom.api

// OpenAIApi.kt
import com.example.retrofitwrooom.model.ChatRequest
import com.example.retrofitwrooom.model.ChatResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer YOUR_API_KEY" // <- Thay bằng key thật
    )
    @POST("v1/chat/completions")
    fun getChatResponse(@Body request: ChatRequest): Call<ChatResponse>
}
