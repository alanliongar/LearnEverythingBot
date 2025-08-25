package com.example.learneverythingbot.data.model

data class ChatMessage(
    val role: Role,
    val text: String
)

enum class Role { User, Assistant }