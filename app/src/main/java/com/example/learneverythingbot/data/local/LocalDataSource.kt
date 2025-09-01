package com.example.learneverythingbot.data.local

import com.example.learneverythingbot.domain.model.ChatHistory
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertChatHistory(chatHistory: ChatHistory)

    fun getAllChatHistory(): Flow<List<ChatHistory>>

    suspend fun deleteAllChat()

    suspend fun deleteChat(id: Int)

    suspend fun getChatById(id: Int): ChatHistory?
}