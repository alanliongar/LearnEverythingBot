package com.example.learneverythingbot.model

data class ChatMessage(
    val role: Role,
    val text: String
)

enum class Role { User, Assistant }