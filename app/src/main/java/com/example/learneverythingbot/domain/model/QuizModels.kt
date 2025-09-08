package com.example.learneverythingbot.domain.model

data class Topic(
    val id: String,
    val name: String,
    val description: String,
    val difficulty: String,
    val questionCount: Int
)

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctAnswer: Int
)