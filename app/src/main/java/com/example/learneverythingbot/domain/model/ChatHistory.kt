package com.example.learneverythingbot.domain.model

data class ChatHistory(
    val id: Int = 0,
    val userMessage: String,
    val aiResponse: String,
    val timestamp: Long = System.currentTimeMillis()
)