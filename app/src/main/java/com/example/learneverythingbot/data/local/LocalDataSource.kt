package com.example.learneverythingbot.data.local

import com.example.learneverythingbot.domain.model.ChatHistoryItem
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertChatHistory(chatHistoryItem: ChatHistoryItem)

    suspend fun insertTopicHistory(chatHistoryItem: ChatHistoryItem)

    suspend fun deleteAllTopic()

    suspend fun deleteTopic(id: Int)

    suspend fun getAllChatHistory(): Flow<List<ChatHistoryItem>>

    suspend fun deleteAllChat()

    suspend fun deleteChat(id: Int)

    suspend fun getChatById(id: Int): ChatHistoryItem?
}