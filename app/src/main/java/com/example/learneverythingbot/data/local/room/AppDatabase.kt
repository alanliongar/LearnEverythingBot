package com.example.learneverythingbot.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.learneverythingbot.data.local.room.ChatHistoryDao
import com.example.learneverythingbot.data.local.room.ChatHistoryEntity

@Database(
    entities = [ChatHistoryEntity::class, TopicSummaryEntity::class, QuizEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatHistoryDao(): ChatHistoryDao

    abstract fun topicSummaryDao(): TopicSummaryDao

    abstract fun quizDao(): QuizDao
}