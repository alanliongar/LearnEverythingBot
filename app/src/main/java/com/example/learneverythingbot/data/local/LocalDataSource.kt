package com.example.learneverythingbot.data.local

import com.example.learneverythingbot.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertTopicHistory(historyItem: HistoryItem)

    fun getAllChatHistory(): Flow<List<HistoryItem>>

    suspend fun deleteAllTopic()

    suspend fun deleteTopic(id: Int)
}