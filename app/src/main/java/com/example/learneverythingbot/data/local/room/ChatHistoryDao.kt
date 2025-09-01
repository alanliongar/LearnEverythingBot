package com.example.learneverythingbot.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.learneverythingbot.data.local.room.ChatHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDao {
    @Insert
    suspend fun insertChatHistory(chatHistory: ChatHistoryEntity)

    @Query("SELECT * FROM chat_history ORDER BY timestamp DESC")
    fun getAllChatHistory(): Flow<List<ChatHistoryEntity>>

    @Query("DELETE FROM chat_history WHERE id = :id")
    suspend fun deleteChat(id: Int)

    @Query("DELETE FROM chat_history")
    suspend fun deleteAllChat()

    @Query("SELECT * FROM chat_history WHERE id = :id")
    suspend fun getChatById(id: Int): ChatHistoryEntity?
}