package com.example.learneverythingbot.domain.model

data class ChatMessage(
    val role: Role,
    val text: String
)

enum class Role { User, Assistant }