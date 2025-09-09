package com.example.learneverythingbot.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(questions: List<QuizEntity>)

    @Query("SELECT * FROM quiz_entity WHERE parent_user_message = :topic")
    suspend fun getQuestionsByTopic(topic: String): List<QuizEntity>?

    @Query("SELECT * FROM quiz_entity WHERE sub_topic = :subTopic AND parent_user_message = :topic")
    suspend fun getQuestionsByTopicAndSubTopic(topic: String, subTopic: String): List<QuizEntity>?

    @Query("SELECT COUNT(*) FROM quiz_entity WHERE parent_user_message = :topic")
    suspend fun countByTopic(topic: String): Int
}