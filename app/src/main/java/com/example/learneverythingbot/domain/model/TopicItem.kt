package com.example.learneverythingbot.domain.model

import java.util.UUID

data class TopicItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val level: Int = 0 // 0 = raiz, 1 = sub, 2 = sub-sub...
)