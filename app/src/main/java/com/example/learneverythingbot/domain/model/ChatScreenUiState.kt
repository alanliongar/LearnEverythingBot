package com.example.learneverythingbot.domain.model

data class ChatScreenUiState(
    val chat: Chat = Chat("", ""),
    val isLoading: Boolean = false,
    val error: String? = null,
    val errorMessage: String? = "Algo deu errado!"
)



data class Chat(
    val subject: String,
    val aiAnswer: String,
    val timeStamp: Long = System.currentTimeMillis()
)

