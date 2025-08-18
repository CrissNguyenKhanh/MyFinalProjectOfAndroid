package com.example.retrofitwrooom.model

data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>
)
