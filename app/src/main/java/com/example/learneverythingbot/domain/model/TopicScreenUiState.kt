package com.example.learneverythingbot.domain.model

data class TopicScreenUiState(
    val chat: Topic = Topic("", "", System.currentTimeMillis()),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = "Algo deu errado!"
)

data class Topic(
    val subject: String,
    val aiAnswer: String,
    val timeStamp: Long
)