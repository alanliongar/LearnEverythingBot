package com.example.learneverythingbot.domain.model

data class TopicHistoryDrawerUiState(
    val topicHistoryItems: List<HistoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = "Algo deu errado!"
)