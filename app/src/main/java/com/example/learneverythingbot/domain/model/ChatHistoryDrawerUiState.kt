package com.example.learneverythingbot.domain.model

data class ChatHistoryDrawerUiState(
    val chatHistoryItems: List<ChatHistoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = "Algo deu errado!"
)