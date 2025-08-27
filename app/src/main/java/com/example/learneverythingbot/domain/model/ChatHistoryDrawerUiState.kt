package com.example.learneverythingbot.domain.model

data class ChatHistoryDrawerUiState(
    val chatHistory: List<ChatHistory> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = "Algo deu errado!"
)