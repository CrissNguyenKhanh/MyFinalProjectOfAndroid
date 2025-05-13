package com.example.retrofitwrooom.model

data class NewResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)