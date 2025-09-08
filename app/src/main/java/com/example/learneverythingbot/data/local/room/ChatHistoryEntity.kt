package com.example.learneverythingbot.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_history")
data class ChatHistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_message")
    val userMessage: String,

    @ColumnInfo(name = "ai_response")
    val aiResponse: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

