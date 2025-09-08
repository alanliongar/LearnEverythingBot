package com.example.learneverythingbot.data.local

import com.example.learneverythingbot.domain.model.ChatHistoryItem
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertChatHistory(chatHistoryItem: ChatHistoryItem)

    suspend fun insertTopicHistory(chatHistoryItem: ChatHistoryItem)

    suspend fun deleteAllTopic()

    suspend fun deleteTopic(topic: String)

    suspend fun getAllChatHistory(): Flow<List<ChatHistoryItem>>

    suspend fun deleteAllChat()

    suspend fun deleteChat(topic: String)

    suspend fun getChatByTopic(topic: String): ChatHistoryItem?

    suspend fun getSummaryByTopicAndSubTopic(topic: String, subTopic: String): String?

    suspend fun insertSummary(topic: String, subTopic: String, secondAiResponse: String)
}