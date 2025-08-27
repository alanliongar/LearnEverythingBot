package com.example.learneverythingbot.domain.model

data class ChatScreenUiState(
    val chat: Chat = Chat("","", System.currentTimeMillis()),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = "Algo deu errado!"
)

data class Chat(
    val subject: String,
    val aiAnswer: String,
    val timeStamp: Long
)

