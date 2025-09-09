package com.example.learneverythingbot.domain.model

data class QuizQuestion(
    val stem: String,
    val level: Int,
    val rightAnswer: String,
    val isRight: Boolean? = null,
)