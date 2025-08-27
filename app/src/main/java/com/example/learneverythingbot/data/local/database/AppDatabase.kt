package com.example.learneverythingbot.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.learneverythingbot.data.local.dao.ChatHistoryDao
import com.example.learneverythingbot.data.local.entity.ChatHistoryEntity

@Database(
    entities = [ChatHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatHistoryDao(): ChatHistoryDao
}