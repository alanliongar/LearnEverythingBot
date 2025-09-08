package com.example.learneverythingbot.domain.model

import java.util.UUID


data class  SubTopics(
    val id: String = UUID.randomUUID().toString(),
    val parentTopic: String,
    val title: String,
    val level : Int,
    val content: String= "", // sera preenchido quando clicado
val  isLoading: Boolean = false,
    )

data class ChatMessage(
    val role: Role,
    val text: String,
    val subTopics: List<SubTopics> = emptyList() // subtopicos identificados na resposta do GPT
)

enum class Role { User, Assistant }