package com.example.learneverythingbot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_history")
data class ChatHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name= "user_message") val userMessage: String,
    @ColumnInfo(name= "ai_response") val aiResponse: String,
    @ColumnInfo(name= "timestamp") val timestamp: Long = System.currentTimeMillis()
)