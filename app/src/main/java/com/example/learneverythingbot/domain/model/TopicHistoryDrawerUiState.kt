package com.example.learneverythingbot.domain.model

data class TopicHistoryDrawerUiState(
    val topicChatHistoryItems: List<ChatHistoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = "Algo deu errado!"
)