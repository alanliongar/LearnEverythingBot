package com.example.learneverythingbot.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "topic_summary",
    foreignKeys = [
        ForeignKey(
            entity = ChatHistoryEntity::class,
            parentColumns = ["user_message"],
            childColumns = ["chat_history_user_message"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["chat_history_user_message", "subtopic_title"], unique = true)
    ]
)
data class TopicSummaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "chat_history_user_message") val chatHistoryUserMessage: String,
    @ColumnInfo(name = "subtopic_title") val subtopicTitle: String,
    @ColumnInfo(name = "second_ai_response") val secondAiResponse: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)
