package com.example.learneverythingbot.domain.model

data class HistoryItem(
    val id: Int = 0,
    val userMessage: String,
    val aiResponse: String,
    val timestamp: Long = System.currentTimeMillis()
)