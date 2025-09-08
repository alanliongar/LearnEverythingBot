package com.example.learneverythingbot.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopicSummaryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(summary: TopicSummaryEntity): Long

    @Query("SELECT * FROM topic_summary WHERE chat_history_user_message = :topic AND subtopic_title = :subtopicTitle LIMIT 1")
    suspend fun getSummary(topic: String, subtopicTitle: String): TopicSummaryEntity?
}