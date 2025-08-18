package com.example.retrofitwrooom.model

data class ChatMessage(
    val message: String,
    val isSentByUser: Boolean // true nếu là bệnh nhân gửi, false là bác sĩ
)
