package com.example.learneverythingbot.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDao {
    @Insert
    suspend fun insertChatHistory(chatHistory: ChatHistoryEntity): Long

    @Query("SELECT * FROM chat_history ORDER BY timestamp DESC")
    fun getAllChatHistory(): Flow<List<ChatHistoryEntity>>

    @Query("DELETE FROM chat_history WHERE user_message = :userMessage")
    suspend fun deleteChat(userMessage: String)

    @Query("DELETE FROM chat_history")
    suspend fun deleteAllChat()

    @Query("SELECT * FROM chat_history WHERE user_message = :topic")
    suspend fun getChatByTopic(topic: String): ChatHistoryEntity?

}