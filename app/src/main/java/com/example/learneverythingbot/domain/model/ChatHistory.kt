package com.example.learneverythingbot.domain.model

import com.example.learneverythingbot.data.local.entity.ChatHistoryEntity

data class ChatHistory (
    val id: Int=0,
    val userMessage: String,
    val aiResponse: String,
    val timestamp: Long =System.currentTimeMillis()
){
    companion object {
        fun fromEntity(entity: ChatHistoryEntity): ChatHistory {
            return ChatHistory(
                id = entity.id,
                userMessage = entity.userMessage,
                aiResponse = entity.aiResponse,
                timestamp = entity.timestamp
            )
        }

        fun toEntity(chatHistory: ChatHistory): ChatHistoryEntity {
            return ChatHistoryEntity(
                id = chatHistory.id,
                userMessage = chatHistory.userMessage,
                aiResponse = chatHistory.aiResponse,
                timestamp = chatHistory.timestamp
            )
        }
    }
}